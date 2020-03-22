import { Moment } from 'moment';

export interface IEntry {
  id?: string;
  province?: string;
  country?: string;
  lastUpdate?: Moment;
  confirmed?: number;
  deaths?: number;
  recovered?: number;
  lat?: number;
  lon?: number;
}

export class Entry implements IEntry {
  constructor(
    public id?: string,
    public province?: string,
    public country?: string,
    public lastUpdate?: Moment,
    public confirmed?: number,
    public deaths?: number,
    public recovered?: number,
    public lat?: number,
    public lon?: number
  ) {}
}
