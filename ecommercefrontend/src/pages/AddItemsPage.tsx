import React, {useState} from "react";
import "../styles/AddItemsPage.css"
import {addItem, patchItem} from "../api/inventory";
import type {UpdateItemDTO} from "../types/UpdateItemDTO";
import type {AddItemDTO} from "../types/AddItemDTO";

export enum ItemType {
    MOUSE = "MOUSE",
    KEYBOARD = "KEYBOARD",
    MONITOR = "MONITOR",
    GPU = "GPU",
    PSU = "PSU",
    PROCESSOR = "PROCESSOR",
    RAM = "RAM",
    COOLER = "COOLER",
}


const ItemForms: React.FC = () => {
    const [addData, setAddData] = useState<AddItemDTO>({
        itemType: ItemType.MOUSE,
        model: "",
        price: 0,
        quantity: 0,
    });

    const [updateData, setUpdateData] = useState<UpdateItemDTO>({
        itemId: 0,
        itemType: ItemType.MOUSE,
        model: "",
        price: 0,
        quantity: 0,
    });

    const [addErrors, setAddErrors] = useState<Partial<Record<keyof AddItemDTO, string>>>({});
    const [updateErrors, setUpdateErrors] = useState<Partial<Record<keyof UpdateItemDTO, string>>>({});

    const handleAddChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const {name, value} = e.target;
        setAddData(prev => ({
            ...prev,
            [name]: name === "price" || name === "quantity" ? Number(value) : value,
        }));
    };

    const handleUpdateChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const {name, value} = e.target;
        setUpdateData(prev => ({
            ...prev,
            [name]: name === "price" || name === "quantity" || name === "itemId" ? Number(value) : value,
        }));
    };

    const validateAdd = (): boolean => {
        const errors: Partial<Record<keyof AddItemDTO, string>> = {};
        if (!addData.itemType) errors.itemType = "Item type is required";
        if (!addData.model.trim()) errors.model = "Model is required";
        if (addData.price < 0) errors.price = "Price cannot be negative";
        if (addData.quantity < 0) errors.quantity = "Quantity cannot be negative";
        setAddErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const validateUpdate = (): boolean => {
        const errors: Partial<Record<keyof UpdateItemDTO, string>> = {};
        if (!updateData.itemId) errors.itemId = "Item ID is required";
        if (updateData.price < 0) errors.price = "Price cannot be negative";
        if (updateData.quantity < 0) errors.quantity = "Quantity cannot be negative";
        setUpdateErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleAddSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateAdd()) return;

        try {
            const response = await addItem(addData)

            const item = await response.json();
            alert(`Item added successfully: ${item.model}`);
            setAddData({itemType: ItemType.MOUSE, model: "", price: 0, quantity: 0});
            setAddErrors({});
        } catch (err) {
            console.error(err);
            alert("Unexpected error adding item");
        }
    };

    const handleUpdateSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateUpdate()) return;

        try {
            const response = await patchItem(updateData);

            if (!response.ok) {
                const msg = await response.text();
                alert("Update item error: " + msg);
                return;
            }

            const item = await response.json();
            alert(`Item updated successfully: ${item.model}`);
            setUpdateData({itemId: 0, itemType: ItemType.MOUSE, model: "", price: 0, quantity: 0});
            setUpdateErrors({});
        } catch (err) {
            console.error(err);
            alert("Unexpected error updating item");
        }
    };

    return (
        <div className="item-form-parent">
            {/* Add Item Form */}
            <form onSubmit={handleAddSubmit} className="form-container">
                <h2>Add New Item</h2>

                <div className="form-field">
                    <label>Item Type</label>
                    <select name="itemType" value={addData.itemType} onChange={handleAddChange}>
                        <option value="">Select type</option>
                        {Object.values(ItemType).map(type => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                    {addErrors.itemType && <p className="error">{addErrors.itemType}</p>}
                </div>

                <div className="form-field">
                    <label>Model</label>
                    <input type="text" name="model" value={addData.model} onChange={handleAddChange}/>
                    {addErrors.model && <p className="error">{addErrors.model}</p>}
                </div>

                <div className="form-field">
                    <label>Price</label>
                    <input type="number" name="price" value={addData.price} onChange={handleAddChange} min={0}/>
                    {addErrors.price && <p className="error">{addErrors.price}</p>}
                </div>

                <div className="form-field">
                    <label>Quantity</label>
                    <input type="number" name="quantity" value={addData.quantity} onChange={handleAddChange} min={0}/>
                    {addErrors.quantity && <p className="error">{addErrors.quantity}</p>}
                </div>

                <button type="submit">Add Item</button>
            </form>


            {/* Update Item Form */}
            <form onSubmit={handleUpdateSubmit} className="form-container">
                <h2>Update Item</h2>

                <label>Item ID</label>
                <input type="number" name="itemId" value={updateData.itemId} onChange={handleUpdateChange}/>
                {updateErrors.itemId && <p className="error">{updateErrors.itemId}</p>}

                <label>Item Type</label>
                <select name="itemType" value={updateData.itemType} onChange={handleUpdateChange}>
                    <option value="">Select type</option>
                    {Object.values(ItemType).map(type => (
                        <option key={type} value={type}>{type}</option>
                    ))}
                </select>

                <label>Model</label>
                <input type="text" name="model" value={updateData.model} onChange={handleUpdateChange}/>

                <label>Price</label>
                <input type="number" name="price" value={updateData.price} onChange={handleUpdateChange} min={0}/>

                <label>Quantity</label>
                <input type="number" name="quantity" value={updateData.quantity} onChange={handleUpdateChange} min={0}/>

                <button type="submit">Update Item</button>
            </form>
        </div>
    );
};

export default ItemForms;
