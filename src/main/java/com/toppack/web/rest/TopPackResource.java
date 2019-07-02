/**
 * 
 */
package com.toppack.web.rest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.toppack.domain.Packages;
import com.toppack.domain.Repository;
import com.toppack.repository.PackagesRepository;
import com.toppack.repository.RepositoryRepository;
import com.toppack.service.dto.PackageRepositoryDTO;
import com.toppack.web.rest.errors.BadRequestAlertException;
import com.toppack.web.rest.util.HeaderUtil;

/**
 * @author ACER
 *
 */
@RestController
@RequestMapping("/api")
public class TopPackResource {

	private final Logger log = LoggerFactory.getLogger(PackagesResource.class);
	private final PackagesRepository packageRepository;
	private final RepositoryRepository repositoryRepository;

	public TopPackResource(PackagesRepository packageRepository, RepositoryRepository repositoryRepository) {
		this.packageRepository = packageRepository;
		this.repositoryRepository = repositoryRepository;
	}

	@GetMapping("/gitrepos/{reponame}")
	@Timed
	public List<Repository> getAllRepos(@PathVariable String reponame)
			throws ClientProtocolException, IOException, JSONException {
		log.debug("REST request to get all github urls");
		// TODO Check if this repo name is already called in last 1min to avoid flooding

		CloseableHttpResponse response = httpResponseClient("https://api.github.com/search/repositories?q=" + reponame);
		JSONObject myObject = getJsonObjectFromResponse(response);

		List<Repository> searchRepos = new ArrayList<Repository>();

		try {
			JSONArray gitRepos = myObject.getJSONArray("items");

			for (int i = 0; i < gitRepos.length(); i++) {
				Repository r = mapRepoEntityToRepoJsonObj(gitRepos, i);
				searchRepos.add(r);
			}

		} finally {
			response.close();
		}

		// TODO check if repo is already existing in db and mark as existing

		return searchRepos;
	}

	
    @GetMapping("/toppackages")
    @Timed
    public List<Packages> getTopPackagesAndRepos() {
        log.debug("REST request to get all Repositories");
        List<Packages> packages = packageRepository.getTopPackages();
//        for (Packages pack : packages) {
//        	PackageRepositoryDTO pacdto = new PackageRepositoryDTO();
//        	pacdto.setP(pack);
//        	pacdto.setRs(null);
//		}
        return packages;
        }
    
	/**
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject getJsonObjectFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
		HttpEntity entity = response.getEntity();
		InputStream instream = entity.getContent();
		JSONObject myObject = new JSONObject(convert(instream));
		return myObject;
	}

	/**
	 * @param reponame
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private CloseableHttpResponse httpResponseClient(String url) throws IOException, ClientProtocolException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		CloseableHttpResponse response = httpclient.execute(httpGet);
		return response;
	}

	@PostMapping("/import")
	@Timed
	public String importRepository(@RequestBody Repository repository)
			throws URISyntaxException, ClientProtocolException, IOException, JSONException {
		log.debug("REST request to import Repository : {}", repository);
		String endMessage = "Something bad happend, i am not modified?";
		if (repository.getId() != null) {
			throw new BadRequestAlertException("A new repository cannot already have an ID", "Repository", "idexists");
		}
		Repository resultRepository = repositoryRepository.save(repository);
		CloseableHttpResponse response = httpResponseClient(
				"https://api.github.com/repos/" + resultRepository.getName() + "/contents/package.json");
		try {
			ArrayList<Packages> packagesList = new ArrayList<Packages>();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				JSONObject packageJsonObj = getJsonObjectFromResponse(response);
				String str = packageJsonObj.getString("content");
				String packageContent = StringUtils.newStringUtf8(Base64.decodeBase64(str));
				JSONObject packageContentJson = new JSONObject(packageContent);
				getListOfDependencies("dependencies",resultRepository, packagesList, packageContentJson);
				getListOfDependencies("devDependencies",resultRepository, packagesList, packageContentJson);
				packageRepository.save(packagesList);
				endMessage = "Ok got the packages!";
			} else {
				endMessage = "Project Does not contain package.json file!";
			}
		} finally {
			response.close();
		}
		return endMessage;
	}

	/**
	 * @param resultRepository
	 * @param packagesList
	 * @param packageContentJson
	 * @throws JSONException
	 */
	private void getListOfDependencies(String dependenciesKey,Repository resultRepository, ArrayList<Packages> packagesList,
			JSONObject packageContentJson) throws JSONException {
		if(packageContentJson.has(dependenciesKey));
		{
			JSONObject dependenciesJson = packageContentJson.getJSONObject(dependenciesKey);
			Iterator<String> dependencies = dependenciesJson.keys();
			while(dependencies.hasNext()) {
		         String depend = dependencies.next();
		         Packages p = new Packages();
		         p.setName(depend);
		         p.setRepository(resultRepository);
		         packagesList.add(p);
		      }
		}
	}

	/**
	 * @param gitRepos
	 * @param i
	 * @return
	 * @throws JSONException
	 */
	private Repository mapRepoEntityToRepoJsonObj(JSONArray gitRepos, int i) throws JSONException {
		JSONObject gitRepo = gitRepos.getJSONObject(i);
		Repository r = new Repository();
		r.setName(gitRepo.getString("full_name"));
		r.setStars(gitRepo.getInt("stargazers_count"));
		r.setForks(gitRepo.getInt("forks_count"));
		return r;
	}

	public String convert(InputStream inputStream) throws IOException {

		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}

		return stringBuilder.toString();
	}
}
