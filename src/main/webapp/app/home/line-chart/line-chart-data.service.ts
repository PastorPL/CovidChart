import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';

@Injectable({
  providedIn: 'root'
})
export class LineChartDataService {
  private data: ChartDataSets[] = [];
  private labels: Label[] = [];

  dataSubject = new Subject<ChartDataSets[]>();
  labelSubject = new Subject<Label[]>();

  constructor() {}

  addData(data: ChartDataSets): void {
    this.data.push(data);
    this.dataSubject.next(this.data.slice());
  }

  setLabel(label: Label[]): void {
    this.labels = label;
    this.labelSubject.next(this.labels.slice());
  }
}
