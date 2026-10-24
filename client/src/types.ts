export type Slot={
    id:number;
    date:string;
    start:string;
    duration:number;
    booked:boolean;
};

export type Service = "HAIR" | "MAKEUP" | "NAILS";