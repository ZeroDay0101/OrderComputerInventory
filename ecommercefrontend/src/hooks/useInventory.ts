import {useEffect, useState} from "react";
import {getInventory} from "../api/inventory.ts";
import type {Item} from "../types/Item.ts"; // your type definition
// your type definition

export const useInventory = () => {
    const [inventory, setInventory] = useState<Item[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchInventory = async () => {
            try {
                const data: Item[] = await getInventory();
                setInventory(data)
            } catch (err: any) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchInventory();
    }, []);

    return {inventory, loading, error};
};
