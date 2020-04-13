import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LineChartDataService {
  dataSeries: { series: SeriesItem[]; name: string }[] = [];
  dataSubject = new Subject<{ series: SeriesItem[]; name: string }[]>();

  constructor() {}

  addSeries(newData: { series: SeriesItem[]; name: string }): void {
    this.dataSeries.push(newData);
    this.dataSubject.next(this.dataSeries.slice());
  }

  getSeries(): { series: SeriesItem[]; name: string }[] {
    return this.dataSeries.slice();
  }

  reset(): void {
    this.dataSeries = [];
    this.dataSubject.next(this.dataSeries.slice());
  }
}

export interface SeriesItem {
  name: string;
  value: number;
}
