import { useEffect, useState } from "react";
import { getSlots } from "./api";
import BookingForm from "./components/BookingForm";
import SlotRow from "./components/SlotRow";
import type { BookingResult, Slot } from "./types";
import HostPage from "./components/HostPage";

const formatDay = (date: string) =>
    new Date(date + "T00:00").toLocaleDateString(undefined, {
        weekday: "long", month: "long", day: "numeric",
    });

function groupByDate(slots: Slot[]) {
    const groups: Record<string, Slot[]> = {};
    for (const slot of slots) (groups[slot.date] ??= []).push(slot);
    return groups;
}

export default function App() {
    const [slots, setSlots] = useState<Slot[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [openId, setOpenId] = useState<number | null>(null);
    const [confirmed, setConfirmed] = useState<BookingResult | null>(null);
    const [view, setView] = useState<"book" | "host">("book");

    const load = () =>
        getSlots()
            .then(setSlots)
            .catch((e: Error) => setError(e.message))
            .finally(() => setLoading(false));

    useEffect(() => { load(); }, []);

    function handleBooked(result: BookingResult) {
        setConfirmed(result);
        setOpenId(null);
        load();
    }

    if (loading) return <p>Loading slots…</p>;
    if (error) return <p>{error}</p>;

    return (
        <main>
            <nav className="tabs">
                <button className="chip" aria-pressed={view === "book"} onClick={() => setView("book")}>Book</button>
                <button className="chip" aria-pressed={view === "host"} onClick={() => setView("host")}>Host</button>
            </nav>

            {view === "host" ? (
                <>
                    <h1>Publish your availability</h1>
                    <p className="lede">Add the times clients can book.</p>
                    <HostPage slots={slots} onChanged={load} />
                </>
            ) : (
                <>
                    <h1>Book your appointment</h1>
                    <p className="lede">Pick a time, then choose your service.</p>

                    {confirmed && (
                        <p className="notice" role="status">
                            {confirmed.message || "Your booking is confirmed. See you soon!"}
                        </p>
                    )}

                    {slots.length === 0 && <p>No slots yet. Check back soon.</p>}

                    {Object.entries(groupByDate(slots)).map(([date, daySlots]) => (
                        <section key={date}>
                            <h2>{formatDay(date)}</h2>
                            {daySlots.map((slot) => (
                                <SlotRow
                                    key={slot.id}
                                    slot={slot}
                                    open={openId === slot.id}
                                    onToggle={() => { setConfirmed(null); setOpenId(openId === slot.id ? null : slot.id); }}
                                >
                                    <BookingForm slot={slot} onBooked={handleBooked} onDone={load} />
                                </SlotRow>
                            ))}
                        </section>
                    ))}
                </>
            )}
        </main>
    );
}