import { useEffect, useState } from "react";
import { getSlots } from "./api";
import BookingForm from "./components/BookingForm";
import SlotRow from "./components/SlotRow";
import type { BookingResult, Slot } from "./types";
import HostPage from "./components/HostPage";
import ServiceCards from "./components/ServiceCards.tsx";

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
    const dateParts = (date: string) => {
        const d = new Date(date + "T00:00");
        return {
            dow: d.toLocaleDateString(undefined, { weekday: "short" }),
            dom: d.getDate(),
            mon: d.toLocaleDateString(undefined, { month: "short" }),
        };
    };

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

    const next = slots.find((s) => !s.booked);

    return (
        <main>
            <header className="topbar">
                <span className="brand">Slotly</span>
                <nav className="menu" aria-label="Main">
                    <button aria-current={view === "book" ? "page" : undefined} onClick={() => setView("book")}>
                        Book
                    </button>
                    <button aria-current={view === "host" ? "page" : undefined} onClick={() => setView("host")}>
                        Host
                    </button>
                </nav>
            </header>

            {view === "host" ? (
                <>
                    <h1>Publish your availability</h1>
                    <p className="lede">Add the times clients can book.</p>
                    <HostPage slots={slots} onChanged={load} />
                </>
            ) : (
                <>
                    <header className="hero">
                        <h1>Beauty Studio</h1>
                        <p className="lede">Hair, makeup and nails, booked in a minute. Pick a time that suits you.</p>
                        {next && (
                            <p className="next">
                                Next free: {formatDay(next.date)} at {next.start.slice(0, 5)}
                            </p>
                        )}
                    </header>

                    <ServiceCards />

                    {confirmed && (
                        <p className="notice" role="status">
                            {confirmed.message || "Your booking is confirmed. See you soon!"}
                        </p>
                    )}

                    {slots.length === 0 && <p>No slots yet. Check back soon.</p>}

                    {Object.entries(groupByDate(slots)).map(([date, daySlots]) => {
                        const p = dateParts(date);
                        return (
                            <section key={date} className="day" aria-label={formatDay(date)}>
                                <div className="date-badge">
                                    <span>{p.dow}</span>
                                    <strong>{p.dom}</strong>
                                    <span>{p.mon}</span>
                                </div>
                                <div className="day-slots">
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
                                </div>
                            </section>
                        );
                    })}
                </>
            )}
        </main>
    );
}