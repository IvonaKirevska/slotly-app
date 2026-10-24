import type { BookingResult, Service, Slot } from "./types";

async function request<T>(url: string, init?: RequestInit): Promise<T> {
    const res = await fetch(url, {
        headers: { "Content-Type": "application/json" },
        ...init,
    });
    const text = await res.text();
    let body: any = null;
    try {
        body = text ? JSON.parse(text) : null;
    } catch {
        throw new Error("The server sent an unexpected response.");
    }
    if (!res.ok) throw new Error(body?.error ?? "Something went wrong.");
    return body as T;
}

export type BookingInput = {
    name: string;
    email: string;
    service: Service;
    notes: string;
};

export const getSlots = () => request<Slot[]>("/api/slots");

export const createSlot = (input: { date: string; start: string; duration: number }) =>
    request<Slot>("/api/slots", { method: "POST", body: JSON.stringify(input) });

export const bookSlot = (id: number, input: BookingInput) =>
    request<Slot>(`/api/slots/${id}/book`, {
        method: "POST",
        body: JSON.stringify(input),
    });

export const getConfirmation = (input: {
    customerName: string;
    serviceName: string;
    date: string;
    time: string;
}) =>
    request<BookingResult>("/api/ai/confirmation", {
        method: "POST",
        body: JSON.stringify(input),
    });