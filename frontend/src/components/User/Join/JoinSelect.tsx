import { useState } from "react";
import "../../../fonts/font.css"
import "../css/JoinSelect.css"
import { Outlet, useNavigate } from "react-router-dom";

type UserType = "CUSTOMER" | "SELLER";

const UserTypeSelectForm : React.FC = () => {
    const [userType, setUserType] = useState<UserType>("CUSTOMER");
    const navigate = useNavigate();

    const handleNextStep = () => {
        if(userType === "CUSTOMER"){
            navigate("/join/customerjoin");
        } else if(userType === "SELLER"){
            navigate("/join/sellerjoin");
        }
    }

    return(
        <div className="totalContainer" style={{fontFamily:"LineRg"}}>
            <div className="container">
                <form className="formBox">
                    <div className="radioGroup">
                        <label className="radioLabel">
                            <input
                            className="radioInput"
                            type="radio" 
                            value="customer" 
                            checked={userType==="CUSTOMER"} 
                            onChange={() => setUserType("CUSTOMER")}/>
                            개인 회원
                        </label>

                        <label className="radioLabel">
                            <input 
                            className="radioInput"
                            type="radio" 
                            value="seller" 
                            checked={userType==="SELLER"}
                            onChange={() => setUserType("SELLER")}/>
                            판매 회원
                        </label>
                    </div>
                </form>

                <button type="submit" className="button" style={{fontFamily:"LineRg"}} onClick={handleNextStep}>
                    다음단계
                </button>
            </div>
            <Outlet/>
        </div>
    )
}


export default UserTypeSelectForm