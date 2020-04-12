import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import * as Moment from 'moment';
import { extendMoment } from 'moment-range';

@Injectable({
  providedIn: 'root'
})
export class LineChartDataService {
  dataSeries: { series: SeriesItem[]; name: string }[] = [];
  dataSubject = new Subject<{ series: SeriesItem[]; name: string }[]>();

  constructor() {}

  addSeries(newData: { series: SeriesItem[]; name: string }): void {
    const moment = extendMoment(Moment);
    const start = moment(newData.series[0].name, 'DD/MM/YYYY');
    const end = moment(newData.series[newData.series.length - 1].name, 'DD/MM/YYYY');
    const range = moment.range(start, end);

    for (const day of range.by('day')) {
      console.log(day.format('YYYY-MM-DD'));
    }

    this.dataSeries.push(newData);
    this.dataSubject.next(this.dataSeries.slice());
  }

  getSeries(): { series: SeriesItem[]; name: string }[] {
    return this.dataSeries.slice();
  }
}

export interface SeriesItem {
  name: string;
  value: number;
}
