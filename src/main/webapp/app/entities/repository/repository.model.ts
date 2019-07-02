import { BaseEntity } from './../../shared';

export class Repository implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public stars?: number,
        public forks?: number,
    ) {
    }
}
