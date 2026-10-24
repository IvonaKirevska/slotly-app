import type {Service, Slot} from "./types.ts";

async function request<T>(url: string, init?: RequestInit): Promise<T>{
    const res=await fetch(url, {
        headers: {"Content-Type": "application/json"},
        ...init,
    });

    const body=await res.json().catch(()=>null);
    if (!res.ok) throw new Error(body?.error ?? "Something went wrong.");
    return body as T;
}

export const getSlots=()=>request<Slot[]>("/api/slots");

export type BookingInput = {
    name: string;
    email: string;
    service: Service;
    notes: string;
};

export const bookSlot = (id: number, input: BookingInput) =>
    request<Slot>(`/api/slots/${id}/book`, {
        method: "POST",
        body: JSON.stringify(input),
    });

export const createSlot = (input: { date: string; start: string; duration: number }) =>
    request<Slot>("/api/slots", {
        method: "POST",
        body: JSON.stringify(input),
    });