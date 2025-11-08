export default class IssueStats {
    _major: number;
    _minor: number;
    
    constructor() {
        this._major = 0;
        this._minor = 0;
    }
    
    get major(): number {
        return this._major || 0;
    }

    set major(_major: number) {
        this._major = _major;
    } 

    get minor(): number {
        return this._minor || 0;
    }

    set minor(_minor: number) {
        this._minor = _minor;
    }
}
