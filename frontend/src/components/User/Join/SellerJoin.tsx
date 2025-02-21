import { useNavigate } from "react-router-dom";
import "../css/Join.css"
import { useEffect, useState } from "react";
import axios from "axios";


interface FormData {
    userName: string;
    storeName: string;
    password: string;
    confirmPassword: string;
    isUserNameAvailable: boolean | null; // null: 체크 안됨, true: 사용가능, false: 중복
}

const SellerJoin : React.FC = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState<FormData>({
        userName: "",
        password: "",
        confirmPassword: "",
        storeName: "",
        isUserNameAvailable: null
    });

    const [errors, setErrors] = useState<Partial<FormData>>({});

    const handleChange = (e:React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    useEffect(() => {
        const checkUserNameAvailability = async() => {
            if (formData.userName === "") return;

            try{
                const response = await axios.post("http://localhost:8080/api/v1/user/join/idcheck",
                    formData.userName
                );

                const data = await response.data;
                setFormData((prev) => ({
                    ...prev,
                    isUserNameAvailable: data   // true: 중복O, false: 중복X
                }));
            } catch (error){
                console.error("중복 체크 에러:", error);
                setFormData((prev) => ({...prev, isUserNameAvailable: false}));
            }
        };

        const debounceTimer = setTimeout(() => {
            checkUserNameAvailability();
        }, 500);

        return () => clearTimeout(debounceTimer);
    }, [formData.userName]);

    const validate = (): boolean => {
        const newErrors: Partial<FormData> = {};

        if (!formData.userName){
            newErrors.userName = "아이디를 입력해주세요.";
        } else if (formData.userName.length < 6) {
            newErrors.userName = "아이디는 최소 6자리 이상이어야 합니다.";
        } else if (formData.userName.length >= 6 && formData.isUserNameAvailable == true) {
            newErrors.userName = "중복된 아이디가 존재합니다.";
        }

        if (!formData.password){
            newErrors.password = "비밀번호를 입력해주세요.";
        } else if (formData.password.length < 6) {
            newErrors.password = "비밀번호는 최소 6자리 이상이어야 합니다.";
        }

        if (formData.password !== formData.confirmPassword){
            newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
        }

        if (!formData.storeName){
            newErrors.storeName = "상호명을 입력해주세요.";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }

    const handleSubmit = async (e:React.FormEvent) => {
        e.preventDefault();
        if(validate()){
            try{
                const response = await axios.post("http://localhost:8080/api/v1/user/seller/join", 
                    {userName: formData.userName, password: formData.password, storeName: formData.storeName}
                )
                alert("회원가입이 완료되었습니다.");
                navigate("/category/home");
                
            } catch{
                console.log("회원가입 중 예상치 못한 에러가 발생하였습니다.");
            }
            
        }
    }


    return(
        <div className="totalContainer">
            <div className="container">
                <h2>회원정보를 입력해주세요</h2>
                <form onSubmit={handleSubmit}>        
                    <div className="input_block">
                        <input type="text" name="userName" placeholder="아이디" value={formData.userName} onChange={handleChange}/>
                        {errors.userName && <p style={{color: "red"}}>{errors.userName}</p>}
                    </div>

                    <div className="input_block">
                        <input type="password" name="password" placeholder="비밀번호" value={formData.password} onChange={handleChange}/>
                        {errors.password && <p style={{color: "red"}}>{errors.password}</p>}
                    </div>

                    <div className="input_block">
                        <input type="password" name="confirmPassword" placeholder="비밀번호 확인" value={formData.confirmPassword} onChange={handleChange}/>
                        {errors.confirmPassword && <p style={{color: "red"}}>{errors.confirmPassword}</p>}
                    </div>

                    <div className="input_block">
                        <input type="text" name="storeName"
                        placeholder="상호명" value={formData.storeName} onChange={handleChange}/>
                        {errors.storeName && <p style={{color:"red"}}>{errors.storeName}</p>}
                    </div>

                    <button className="btn" type="submit" style={{fontFamily:"LineRg"}}>회원가입</button>
                </form>
                
            </div>

        </div>
    )
}
export default SellerJoin