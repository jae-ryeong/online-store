import DaumPostcodeEmbed from "react-daum-postcode";
import { useNavigate } from "react-router-dom"

export default function AddressSearch() {
    const nav = useNavigate();

    const handleComplete = (data: any) => {
        const fullAddress = data.address;
        const postalCode = data.zonecode;

        // 주소 정보를 localStorage에 저장
        localStorage.setItem("seletedAddress", JSON.stringify({ fullAddress, postalCode }));

        nav("/popup/addAddress");
    };


    return (
        <div style={{
            margin: 0,
            padding: 0,
            width: "100vw",
            height: "100vh"
        }}>
            <DaumPostcodeEmbed onComplete={handleComplete} style={{ width: "100%", height: "100%" }} />
        </div>
    )
}