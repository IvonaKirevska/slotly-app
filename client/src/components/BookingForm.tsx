import { useState, type FormEvent } from "react";
import {bookSlot, getConfirmation} from "../api";
import type { BookingResult, Service, Slot } from "../types";
import ServicePicker from "./ServicePicker";

type Props = {
    slot: Slot;
    onBooked: (result: BookingResult) => void;
    onDone: () => void;
};

export default function BookingForm({ slot, onBooked, onDone }: Props) {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [service, setService] = useState<Service>("HAIR");
    const [notes, setNotes] = useState("");
    const [error, setError] = useState("");
    const [busy, setBusy] = useState(false);

    async function submit(e: FormEvent) {
        e.preventDefault();
        setBusy(true);
        setError("");
        try {
            await bookSlot(slot.id, { name, email, service, notes });

            // The booking is saved. The AI message is a bonus, so a failure here is ignored.
            let message = `Thanks ${name}, your appointment is confirmed. See you soon!`;
            try {
                const ai = await getConfirmation({
                    customerName: name,
                    serviceName: service.toLowerCase(),
                    date: new Date(slot.date + "T00:00").toLocaleDateString("en-US", {
                        weekday: "long",
                        month: "long",
                        day: "numeric",
                    }),
                    time: slot.start.slice(0, 5),
                });
                if (ai.message) message = ai.message;
            } catch {
                /* keep the fallback message */
            }

            onBooked({ message });
        } catch (err) {
            setError((err as Error).message);
            onDone(); // reload the list in case the slot was just taken
        } finally {
            setBusy(false);
        }
    }

    return (
        <form className="booking" onSubmit={submit}>
            <ServicePicker value={service} onChange={setService} />
            <label>
                Your name
                <input value={name} maxLength={60} onChange={(e) => setName(e.target.value)} required />
            </label>
            <label>
                Email
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </label>
            <label>
                Note (optional)
                <textarea rows={2} value={notes} onChange={(e) => setNotes(e.target.value)} />
            </label>
            {error && <p className="error" role="alert">{error}</p>}
            <button className="btn" disabled={busy}>
                {busy ? "Confirming your booking…" : "Confirm booking"}
            </button>
        </form>
    );
}