import { BaseEntity } from './../../shared';

export class Packages implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public repository?: BaseEntity,
    ) {
    }
}
