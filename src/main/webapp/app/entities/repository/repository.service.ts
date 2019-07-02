import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Repository } from './repository.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RepositoryService {

    private resourceUrl = SERVER_API_URL + 'api/repositories';

    constructor(private http: Http) { }

    create(repository: Repository): Observable<Repository> {
        const copy = this.convert(repository);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(repository: Repository): Observable<Repository> {
        const copy = this.convert(repository);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Repository> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Repository.
     */
    private convertItemFromServer(json: any): Repository {
        const entity: Repository = Object.assign(new Repository(), json);
        return entity;
    }

    /**
     * Convert a Repository to a JSON which can be sent to the server.
     */
    private convert(repository: Repository): Repository {
        const copy: Repository = Object.assign({}, repository);
        return copy;
    }
}
