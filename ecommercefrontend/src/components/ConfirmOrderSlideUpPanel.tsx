import React from "react";
import "../styles/SlideUpPanel.css";


interface SlideUpPanelProps {
    isOpen: boolean;
    onClose: () => void;

    handleSubmit: () => void;

    children: React.ReactNode;
}

const ConfirmOrderSlideUpPanel: React.FC<SlideUpPanelProps> = ({isOpen, onClose, handleSubmit, children}) => {

    return (
        <div className={`slide-panel ${isOpen ? "open" : ""}`}>
            <div className="panel-content">
                {children}
                <button className="close-btn" onClick={onClose}>Close</button>
                <button className="submit-btn" onClick={handleSubmit}>Confirm purchase</button>
            </div>
        </div>
    );
};

export default ConfirmOrderSlideUpPanel;
