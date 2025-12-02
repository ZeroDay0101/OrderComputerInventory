import React, {useEffect} from "react";
import type {Order} from "../types/Order.ts";
import {useAuth} from "../context/useAuth.ts";
import {getUserOrders} from "../api/transaction.ts";
import type {Item} from "../types/Item.ts";
import {getInventory} from "../api/inventory.ts";
import "/src/styles/TransactionHistoryPage.css";


export default function TransactionHistoryPage() {
    const [transactions, setTransactions] = React.useState<Order[]>([]);
    const [items, setItems] = React.useState<Item[]>([]);
    const {user} = useAuth();
    useEffect(() => {
        async function setUserTransactions() {
            if (user?.id) {
                const transactions: Order[] = await getUserOrders(user?.id);
                const items: Item[] = await getInventory();

                setItems(items);
                setTransactions(transactions);
            }
        }

        setUserTransactions();
    }, [user]); //Runs when user changes

    return (
        <div className="transactions">
            <h1>Transaction history</h1>
            {transactions.map((order: Order) => {
                const item = items.find(i => i.id === order.itemId);

                return (
                    <div key={order.transactionId}>
                        <h2>Item Name: {item?.model ?? "Loading..."}</h2>
                        <p>Price: {item?.price ?? "Loading..."}$</p>
                        <p>Quantity: {order.quantity ?? "Loading..."}</p>
                        <p>OrderStatus: {order.status}</p>
                    </div>
                );
            })}
        </div>
    );
}
