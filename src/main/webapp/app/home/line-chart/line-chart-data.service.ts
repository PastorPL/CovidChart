import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';
import { Moment } from 'moment';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class LineChartDataService {
  private data: ChartDataSets[] = [];
  private mySet = new Set();
  private labels: Label[] = [];

  dataSubject = new Subject<ChartDataSets[]>();
  labelSubject = new Subject<Label[]>();

  constructor() {}

  addData(data: ChartDataSets): void {
    this.data.push(data);
    this.dataSubject.next(this.data.slice());
  }

  setLabel(newTimes: Moment[]): void {
    for (const item of newTimes) {
      this.mySet.add(item);
    }

    const sortedMoments = (Array.from(this.mySet.values()) as Moment[]).sort((a, b) => a.valueOf() - b.valueOf());
    for (const item of sortedMoments) {
      /* eslint-disable no-console */
      console.log(item);
      /* eslint-enable no-console */
    }
    this.labels = [];
    for (const momentElement of sortedMoments) {
      this.labels.push(moment(momentElement).format('DD-MM-YYYY'));
    }

    this.labelSubject.next(this.labels.slice());
  }
}
