import { Box, Button, FormControlLabel, Paper, Radio, RadioGroup, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Address {
    id: number;
    addresseeName: string;
    address: string;
    detailAddress: string;
    postalCode: number;
    tel: string;
    isDefault?: boolean;
}

// Address에서 id를 제외하고 새로운 타입을 생성
type RawAddressFromAPI = Omit<Address, "id">;

export default function AddressModal() {
    const [selectedId, setSelectedId] = useState<number>(4);
    const [addresses, setAddresses] = useState<Address[]>([]);

    const nav = useNavigate();
    const getAuth = () => {
        const token = localStorage.getItem("accessToken")

        if (!token) {
            console.error("JWT 토큰이 없습니다. 로그인 해주세요.");
            nav("/category/home");   // 로그인 되어 있지 않을때 메인페이지로 이동
            return;
        }
        return token;
    }

    // 매핑 함수: id만 프론트에서 생성
    const mapAddressWithId = (data: RawAddressFromAPI[]): Address[] => {
        return data.map((item, index) => ({
            ...item,
            id: index + 1, // 1부터 시작
        }));
    }

    useEffect(() => {
        const token = getAuth();

        const fetchAddressData = async () => {
            try {
                const response = await fetch("http://localhost:8080/api/v1/user/setting/address/view", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (!response.ok) {
                    throw new Error("Failed to fetch address data");
                }
                const data = await response.json();
                console.log(data);
                const dataWithId = mapAddressWithId(data); // id 추가
                setAddresses(dataWithId);
            } catch (error) {
                console.error("Error fetching address data:", error);
            }
        }

        fetchAddressData();
    }, [])

    const handleSubmit = () => {
        const selected = addresses.find((addr) => addr.id === selectedId);
        if (selected && window.opener) {
            window.opener.postMessage(
                { type: "ADDRESS_SELECTED", payload: selected },
                window.origin
            );
        }
        window.close();
    }

    return (
        <Box maxWidth="600px" margin="auto" padding={2}>
            <Button variant="contained" color="inherit" fullWidth sx={{ mb: 2 }}>
                배송지 추가하기
            </Button>

            <RadioGroup
                value={selectedId.toString()}
                onChange={(e) => setSelectedId(Number(e.target.value))}
            >
                <Stack spacing={2}>
                    {addresses.map((addr) => (
                        <Paper
                            key={addr.id}
                            variant="outlined"
                            sx={{
                                borderColor: selectedId === addr.id ? "black" : "grey.300",
                                padding: 2,
                            }}
                        >
                            <FormControlLabel
                                value={addr.id.toString()}
                                control={<Radio />}
                                label={
                                    <Box>
                                        <Typography variant="subtitle1">
                                            {addr.addresseeName}{" "}
                                            {addr.isDefault && (
                                                <Typography
                                                    variant="caption"
                                                    component="span"
                                                    color="text.secondary"
                                                >
                                                    최근 사용
                                                </Typography>
                                            )}
                                        </Typography>
                                        <Typography>{addr.address}</Typography>
                                        <Typography>{addr.detailAddress}</Typography>
                                        <Typography>{addr.tel}</Typography>
                                        <Stack direction="row" spacing={1} mt={1}>
                                            <Button size="small" variant="outlined">
                                                수정
                                            </Button>
                                            <Button size="small" variant="outlined">
                                                삭제
                                            </Button>
                                        </Stack>
                                    </Box>
                                }
                            />
                        </Paper>
                    ))}
                </Stack>
            </RadioGroup>

            <Button onClick={handleSubmit} variant="contained" color="inherit" fullWidth sx={{ mt: 3 }}>
                변경하기
            </Button>
        </Box>
    )
}
