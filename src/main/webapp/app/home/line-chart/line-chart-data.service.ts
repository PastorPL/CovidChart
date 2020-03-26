import { Injectable } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
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

  addData(data: ChartDataSets) {
    this.data.push(data);
    this.dataSubject.next(this.data.slice());
  }

  setLabel(label: Label[]) {
    this.labels = label;
    this.labelSubject.next(this.labels.slice());
  }
}
