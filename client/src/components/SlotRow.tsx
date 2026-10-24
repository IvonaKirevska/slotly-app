import type { ReactNode } from "react";
import type { Slot } from "../types";

type Props = { slot: Slot; open: boolean; onToggle: () => void; children?: ReactNode };

export default function SlotRow({ slot, open, onToggle, children }: Props) {
    return (
        <div className={"slot" + (slot.booked ? " taken" : "")}>
            <div className="row">
                <span className="time">{slot.start.slice(0, 5)}</span>
                <span className="meta">{slot.duration} min</span>
                {slot.booked && !open ? (
                    <span className="tag">Taken</span>
                ) : (
                    <button className="btn" onClick={onToggle}>{open ? "Close" : "Book"}</button>
                )}
            </div>
            {open && children}
        </div>
    );
}