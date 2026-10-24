import { useState, type FormEvent } from "react";
import { createSlot } from "../api";
import type { Slot } from "../types";

type Props = { slots: Slot[]; onChanged: () => void };

export default function HostPage({ slots, onChanged }: Props) {
    const [date, setDate] = useState("");
    const [start, setStart] = useState("10:00");
    const [duration, setDuration] = useState(60);
    const [error, setError] = useState("");
    const [busy, setBusy] = useState(false);

    async function submit(e: FormEvent) {
        e.preventDefault();
        setBusy(true);
        setError("");
        try {
            await createSlot({ date, start, duration });
            onChanged();
        } catch (err) {
            setError((err as Error).message);
        } finally {
            setBusy(false);
        }
    }

    return (
        <>
            <form className="booking host-form" onSubmit={submit}>
                <label>
                    Date
                    <input type="date" value={date} onChange={(e) => setDate(e.target.value)} required />
                </label>
                <label>
                    Start time
                    <input type="time" value={start} onChange={(e) => setStart(e.target.value)} required />
                </label>
                <label>
                    Duration (minutes)
                    <input
                        type="number" min={15} max={480} step={15}
                        value={duration} onChange={(e) => setDuration(+e.target.value)} required
                    />
                </label>
                {error && <p className="error" role="alert">{error}</p>}
                <button className="btn" disabled={busy}>{busy ? "Publishing…" : "Publish slot"}</button>
            </form>

            <h2>Published slots</h2>
            {slots.length === 0 && <p className="lede">Nothing published yet. Add your first slot above.</p>}
            {slots.map((s) => (
                <div key={s.id} className={"slot" + (s.booked ? " taken" : "")}>
                    <div className="row">
                        <span className="time">{s.start.slice(0, 5)}</span>
                        <span className="meta">{s.date} · {s.duration} min</span>
                        <span className="tag">{s.booked ? "Booked" : "Open"}</span>
                    </div>
                </div>
            ))}
        </>
    );
}