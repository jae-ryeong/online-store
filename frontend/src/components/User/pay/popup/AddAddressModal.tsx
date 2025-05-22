import { Box, Button, IconButton, TextField } from "@mui/material";
import HomeIcon from '@mui/icons-material/Home';
import PersonIcon from '@mui/icons-material/Person';
import SearchIcon from '@mui/icons-material/Search';
import PhoneIcon from '@mui/icons-material/Phone';
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Addressprops {
    addresseeName: string;
    address: string;
    detailAddress: string;
    postalCode: number;
    tel: string;
}

export default function AddAddress() {
    const nav = useNavigate();
    const [form, setForm] = useState<Addressprops>({
        addresseeName: "",
        address: "",
        detailAddress: "",
        postalCode: 0,
        tel: "",
    });
    const [postalCode, setPostalCode] = useState("");
    const [fullAddress, setFullAddress] = useState("");

    // 필수 값 유효성 검사
    const isFormValid = 
    form.addresseeName && form.address && form.postalCode && form.tel && form.tel


    useEffect(() => {
        const selectedAddress = localStorage.getItem("seletedAddress");
        if (selectedAddress) {
            const { fullAddress, postalCode } = JSON.parse(selectedAddress);
            setFullAddress(fullAddress);
            setPostalCode(postalCode);
            setForm({ ...form, address: fullAddress, postalCode: Number(postalCode) });
        }
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        if (name === "tel") {
            let input = e.target.value.replace(/[^0-9]/g, ""); // 숫자만 남기기

            // 010-xxxx-xxxx 형태로 포맷팅
            if (input.length <= 3) {
                input = input;
            } else if (input.length <= 7) {
                input = `${input.slice(0, 3)}-${input.slice(3)}`;
            } else if (input.length <= 11) {
                input = `${input.slice(0, 3)}-${input.slice(3, 7)}-${input.slice(7)}`;
            } else {
                input = `${input.slice(0, 3)}-${input.slice(3, 7)}-${input.slice(7, 11)}`;
            }
            setForm({ ...form, tel: input });
        } else {
            setForm({ ...form, [name]: value });
        }
        console.log(form);
    };

    const handleSubmit = async () => {
        const token = localStorage.getItem("accessToken");

        if (!token) {
            console.error("JWT 토큰이 없습니다. 로그인 해주세요.");
            nav("/category/home"); // 로그인 되어 있지 않을때 메인페이지로 이동
            return;
        }

        try {
            const response = await fetch(process.env.REACT_APP_API_BASE_URL + "/api/v1/user/setting/address/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(form),
            });

            localStorage.removeItem("seletedAddress"); // 주소 검색 후 저장된 주소 삭제
            nav("/popup/addressmanagement"); // 주소 관리 페이지로 이동
            if (!response.ok) {
                throw new Error("Failed to add address");
            }
            alert("주소가 추가되었습니다.");
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div className="w-full max-w-md mx-auto p-6 bg-white rounded-2xl shadow-md space-y-4">
            <h2 className="text-xl font-semibold text-center">배송지 추가</h2>

            {/* 받는 사람 */}
            <Box display="flex" alignItems="center" className="border rounded-lg px-3 py-1">
                <PersonIcon className="mr-2" />
                <TextField
                    variant="standard"
                    name="addresseeName"
                    placeholder="받는 사람"
                    fullWidth
                    required
                    onChange={handleChange}
                />
            </Box>

            {/* 우편번호 찾기 및 우편번호 */}
            <Box display="flex" alignItems="center" className="border rounded-lg px-3 py-1">
                <HomeIcon className="mr-2" />
                <TextField
                    variant="standard"
                    placeholder="우편번호 찾기"
                    value={postalCode}
                    fullWidth
                    required
                    onChange={handleChange}
                />
                <SearchIcon style={{ cursor: "pointer" }} onClick={() => nav("/popup/addresssearch")} />
            </Box>

            {/* 주소 */}
            <Box display="flex" alignItems="center" className="border rounded-lg px-3 py-1">
                <HomeIcon className="mr-2" />
                <TextField
                    variant="standard"
                    placeholder="주소"
                    value={fullAddress}
                    fullWidth
                    required
                    onChange={handleChange}
                />
            </Box>
            {/* 상세 주소 */}
            <Box display="flex" alignItems="center" className="border rounded-lg px-3 py-1">
                <HomeIcon className="mr-2" />
                <TextField
                    variant="standard"
                    placeholder="상세 주소"
                    name="detailAddress"
                    onChange={handleChange}
                    fullWidth
                />
            </Box>
            {/* 휴대폰 번호 */}
            <Box display="flex" alignItems="center" className="px-3 py-1">
                <PhoneIcon className="mr-2" />
                <TextField
                    type="tel"
                    variant="standard"
                    placeholder="휴대폰 번호"
                    fullWidth
                    required
                    name="tel"
                    onChange={handleChange}
                />
            </Box>

            {/* 저장 버튼 */}
            <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                className="!mt-2 !bg-blue-600 hover:!bg-blue-700"
                onClick={handleSubmit}
                disabled={!isFormValid} // 필수 값이 모두 입력되지 않으면 비활성화
            >
                저장
            </Button>
        </div>
    )
}
