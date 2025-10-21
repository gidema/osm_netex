export default class ChbQuay {
    id!: string;
    stopPlaceId!: string;
    stopPlaceName!: string;
    stopPlaceLongName!: string;
    transportModes!: string[];
    onlygetout!: boolean;
    quayCode!: string;
    quayName!: string;
    stopSideCode!: string;
    validfrom!: Date;
    mutationdate!: Date;
    quayType!: string;
    quayStatus!: string;
//    private Point rdLocation;
//    private Point wgsLocation;
    bearing!: number;
    town!: string;
    level!: string;
    street!: string;
    location!: string;
}
