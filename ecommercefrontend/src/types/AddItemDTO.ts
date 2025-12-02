import {ItemType} from "../pages/AddItemsPage";

export interface AddItemDTO {
    itemType: ItemType;
    model: string
    price: number;
    quantity: number;
}