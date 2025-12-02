import React, {useState} from "react";
import "../styles/OrderForm.css"

type OrderFormProps = {
    onOrder: (quantity: number) => void;
};

export default function OrderForm({onOrder}: OrderFormProps) {
    const [quantity, setQuantity] = useState<number>(1);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onOrder(quantity); //Now the quantity can be visible from the InventoryPage (is passed there)
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="number"
                placeholder="quantity"
                onChange={(event) => setQuantity(parseInt(event.target.value))}
                value={quantity}
                min={1}
                required={true}
            />
            <input type="submit" value="Submit"/>
        </form>
    );
}
