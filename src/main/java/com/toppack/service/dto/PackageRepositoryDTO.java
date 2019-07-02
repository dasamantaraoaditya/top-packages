/**
 * 
 */
package com.toppack.service.dto;
import java.util.List;

import com.toppack.domain.Packages;
import com.toppack.domain.Repository;;
/**
 * @author ACER
 *
 */
public class PackageRepositoryDTO {
	long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Packages getP() {
		return p;
	}
	public void setP(Packages p) {
		this.p = p;
	}
	public List<Repository> getRs() {
		return rs;
	}
	public void setRs(List<Repository> rs) {
		this.rs = rs;
	}
	Packages p;
	List<Repository> rs;
}
