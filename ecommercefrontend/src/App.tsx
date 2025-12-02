import {BrowserRouter, Route, Routes} from "react-router-dom";

import LoginPage from "./pages/LoginPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import InventoryPage from "./pages/InventoryPage.tsx";
import TransactionHistoryPage from "./pages/TransactionHistoryPage.tsx";
import AccountSettingsPage from "./pages/AccountSettingsPage.tsx";
import AddItemsPage from "./pages/AddItemsPage.tsx";
import {AuthProvider} from "./context/AuthProvider.tsx";

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Routes>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                    <Route path="" element={<InventoryPage/>}/>
                    <Route path="/inventory" element={<InventoryPage/>}/>
                    <Route path="/user" element={<AccountSettingsPage/>}/>
                    <Route path="/history" element={<TransactionHistoryPage/>}/>
                    <Route path="/additems" element={<AddItemsPage/>}/>
                </Routes>
            </AuthProvider>
        </BrowserRouter>
    );
}
