import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { IEntry } from 'app/shared/model/entry.model';

@Injectable({
  providedIn: 'root'
})
export class GitHubService {
  constructor(private http: HttpClient) {}

  getCountries(): Observable<string[]> {
    return this.http.get(SERVER_API_URL + 'api/files/countryName') as Observable<string[]>;
  }

  getCountry(country: string): Observable<IEntry[]> {
    return this.http.get(SERVER_API_URL + '/api/files/country/' + country) as Observable<IEntry[]>;
  }
}
