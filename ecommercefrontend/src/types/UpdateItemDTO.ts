import {ItemType} from "../pages/AddItemsPage";

export interface UpdateItemDTO {
    itemId: number;
    itemType: ItemType;
    model: string
    price: number;
    quantity: number;
}