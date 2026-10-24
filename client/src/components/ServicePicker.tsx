import type { Service } from "../types";

const SERVICES: { id: Service; label: string }[] = [
    { id: "HAIR", label: "Hair" },
    { id: "MAKEUP", label: "Makeup" },
    { id: "NAILS", label: "Nails" },
];

export default function ServicePicker({ value, onChange }: { value: Service; onChange: (s: Service) => void }) {
    return (
        <div className="chips" role="group" aria-label="Service">
            {SERVICES.map((s) => (
                <button key={s.id} className="chip" aria-pressed={value === s.id} onClick={() => onChange(s.id)}>
                    {s.label}
                </button>
            ))}
        </div>
    );
}