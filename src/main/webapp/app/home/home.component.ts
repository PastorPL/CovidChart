import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { GitHubService } from 'app/shared/util/git-hub.service';
import { first } from 'rxjs/operators';
import { LineChartDataService } from 'app/home/line-chart/line-chart-data.service';
import { IEntry } from 'app/shared/model/entry.model';
import * as moment from 'moment';
import { Moment } from 'moment';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  files: string[] = [];
  loadingFiles = false;
  lastUpdate = '';

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private githubService: GitHubService,
    private lineChartDataService: LineChartDataService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.githubService.getLastUpdate().subscribe(value => {
      this.lastUpdate = moment(value.lastUpdate).format('D MMM YYYY');
      /* eslint-disable no-console */
      console.log(value);
      /* eslint-enable no-console */
    });
    this.githubService
      .getCountries()
      .pipe(first())
      .subscribe(countries => {
        this.files = countries;
      });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  addChartValue(): void {
    this.loadingFiles = true;
  }

  onNewData(country: string): void {
    this.githubService
      .getCountry(country)
      .pipe(first())
      .subscribe(data => this.updateData(data, country));
  }

  private updateData(entries: IEntry[], country: string): void {
    const dataNumbers: number[] = [];
    // const labels: Label[] = [];
    const updateTimes: Moment[] = [];

    for (const entry of entries) {
      dataNumbers.push(entry.confirmed!);
      updateTimes.push(entry.lastUpdate!);

      // labels.push(moment(entry.lastUpdate).format('DD-MM-YYYY'));
    }

    // for (const entry of entries) {
    //   /* eslint-disable no-console */
    //   console.log(entry);
    //   /* eslint-enable no-console */
    //   dataNumbers.push(entry.confirmed!);
    //   labels.push(entry.lastUpdate?.format('DD-MM-YYYY')!);
    // }

    this.lineChartDataService.addData({ data: dataNumbers, label: country });
    this.lineChartDataService.setLabel(updateTimes);
  }
}
