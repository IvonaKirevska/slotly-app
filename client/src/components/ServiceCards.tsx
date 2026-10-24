const SERVICES = [
    { title: "Hair", text: "Cuts, colour and styling" },
    { title: "Makeup", text: "Everyday looks to full glam" },
    { title: "Nails", text: "Manicures and gel polish" },
];

export default function ServiceCards() {
    return (
        <ul className="services">
            {SERVICES.map((s) => (
                <li key={s.title}>
                    <strong>{s.title}</strong>
                    <span>{s.text}</span>
                </li>
            ))}
        </ul>
    );
}