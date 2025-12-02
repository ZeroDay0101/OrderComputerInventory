import {useState} from "react";
import "/src/styles/InventoryPage.css";
import {Link} from "react-router-dom";
import OrderForm from "../components/OrderForm";
import {useAuth} from "../context/useAuth";
import ConfirmOrderSlideUpPanel from "../components/ConfirmOrderSlideUpPanel.tsx";
import {deleteItem, placeOrder} from "../api/inventory.ts";
import {useInventory} from "../hooks/useInventory.ts";
import UserMenu from "../components/UserMenu.tsx";
import UserAddressDisplay from "../components/UserAddressDisplay.tsx";


interface FinalizeOrder {
    id: number;
    itemType: string;
    model: string;
    price: number;
    quantity: number;
}

type OrderError = {
    detail: string;
}

export default function InventoryPage() {
    const {inventory, loading, error} = useInventory();

    const [orderError, setOrderError] = useState<OrderError | null>();
    const [open, setOpen] = useState(false);
    const [confirmationPopUpItem, setConfirmationPopUpItem] = useState<FinalizeOrder>();
    const {user} = useAuth();


    const handleOrder = async (itemId: number, quantity: number,price:number) => {
        if (!user) {
            window.location.href = "http://localhost:5173/login?error=You+must+be+logged-in+to+place+an+order";
            return;
        }
        try {
            await placeOrder(itemId, quantity);
            setOrderError(null);
            setOpen(false)
            alert('Order placed successfully!')
            user.balance = user.balance - (price * quantity);
        } catch (err: any) {
            setOrderError({detail: err.message});
        }

    };
    const handleDeleteItem = async (itemId: number) => {
        if (!user || user.role !== "ADMIN") return;

        if (!confirm("Are you sure you want to delete this item?")) return;

        try {
            await deleteItem(itemId);
            alert("Item deleted successfully");
            window.location.reload(); // easiest refresh
        } catch (err: any) {
            alert("Error deleting item: " + err.message);
        }
    };

    if (loading || error) {
        return (
            <div className="center-container">
                <p className={error ? "error-msg" : "loading-msg"}>
                    {loading ? "Loading inventory..." : `Error: ${error}`}
                </p>
            </div>
        );
    }

    return (
        <div className="inventory-page">
            <header className="inventory-header">
                <h1 style={{textAlign: "center"}}>Inventory Dashboard</h1>
                {user ? (
                    <div className="user-menu">
                        Balance: ${user.balance}
                        <UserMenu user={user}></UserMenu>
                    </div>
                ) : (
                    <Link to="http://localhost:5173/login" className="login-button">
                        Log In
                    </Link>
                )}
            </header>

            <div className="inventory-container">
                {inventory.filter(item => item.itemStatus === "ACTIVE").length === 0 ? (
                    <p className="no-items-msg">No active items available</p>
                ) : (
                    inventory
                        .filter(item => item.itemStatus === "ACTIVE")
                        .map((item) => (
                            <div key={item.id} className="inventory-card">
                                <h2>{item.model}</h2>
                                {user?.role === "ADMIN" && <p>ID: {item.id}</p>}
                                <p>Type: {item.itemType}</p>
                                <p>Price: ${item.price}</p>
                                <p>Stock: {item.quantity}</p>

                                <div className="order-and-quantity">
                                    <OrderForm
                                        onOrder={(quantity) => {
                                            setOpen(true);
                                            setConfirmationPopUpItem({
                                                id: item.id,
                                                model: item.model,
                                                itemType: item.itemType,
                                                price: item.price,
                                                quantity,
                                            });
                                        }}
                                    />
                                </div>
                                {user?.role === "ADMIN" && (
                                    <button
                                        style={{
                                            marginTop: "10px",
                                            background: "#b61e1e",
                                            color: "white",
                                            padding: "8px",
                                            borderRadius: "6px",
                                            cursor: "pointer",
                                        }}
                                        onClick={() => handleDeleteItem(item.id)}
                                    >
                                        Delete Item
                                    </button>
                                )}
                            </div>
                        ))
                )}
            </div>

            {confirmationPopUpItem && (
                <ConfirmOrderSlideUpPanel
                    isOpen={open}
                    onClose={() => setOpen(false)}
                    handleSubmit={() => handleOrder(confirmationPopUpItem?.id, confirmationPopUpItem.quantity,confirmationPopUpItem.price)}
                >
                    <div className="error-message">
                        <h2>
                            {orderError && <p className="order-error">{orderError.detail}</p>}
                        </h2>
                    </div>
                    <div className="popup-details">
                        <h2>{confirmationPopUpItem.model}</h2>
                        <p>Type: {confirmationPopUpItem.itemType}</p>
                        <p>Price: ${confirmationPopUpItem.price}</p>
                        <p>Quantity: {confirmationPopUpItem.quantity}</p>

                        <UserAddressDisplay userId={user?.id}/>
                    </div>
                </ConfirmOrderSlideUpPanel>
            )}

        </div>

    );

}
