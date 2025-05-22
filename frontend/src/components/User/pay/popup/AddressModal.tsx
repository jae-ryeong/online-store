import { Box, Button, FormControlLabel, Paper, Radio, RadioGroup, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Address {
    addressId: number;
    addresseeName: string;
    address: string;
    detailAddress: string;
    postalCode: number;
    tel: string;
    isDefault?: boolean;
}

export default function AddressModal() {
    const [selectedId, setSelectedId] = useState<number>(0);
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

    useEffect(() => {
        const token = getAuth();

        const fetchAddressData = async () => {
            try {
                const response = await fetch(process.env.REACT_APP_API_BASE_URL + "/api/v1/user/setting/address/view", {
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
                setAddresses(data);
            } catch (error) {
                console.error("Error fetching address data:", error);
            }
        }

        fetchAddressData();
    }, [])

    // 배송지 선택 완료
    const completeSubmit = () => {
        const selected = addresses.find((addr) => addr.addressId === selectedId);
        if (selected && window.opener) {
            window.opener.postMessage(
                { type: "ADDRESS_SELECTED", payload: selected },
                window.origin
            );
        }
        window.close();
    }

    // 배송지 삭제
    const deleteAddress = async (addressId: number) => {
        const token = getAuth();
        try {
            const response = await fetch(process.env.REACT_APP_API_BASE_URL + `/api/v1/user/setting/address/delete/${addressId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            if (!response.ok) {
                throw new Error("Failed to delete address");
            }
            setAddresses((prev) => prev.filter((addr) => addr.addressId !== addressId));
            window.location.reload(); // 페이지 새로고침
        } catch (error) {
            console.error("Error deleting address:", error);
        }
    }

    return (
        <Box maxWidth="600px" margin="auto" padding={2}>
            <Button variant="contained" color="inherit" fullWidth sx={{ mb: 2 }} onClick={() => {nav("/popup/addAddress")}}>
                배송지 추가하기
            </Button>

            <RadioGroup
                value={selectedId.toString()}
                onChange={(e) => setSelectedId(Number(e.target.value))}
            >
                <Stack spacing={2}>
                    {addresses.map((addr) => (
                        <Paper
                            key={addr.addressId}
                            variant="outlined"
                            sx={{
                                borderColor: selectedId === addr.addressId ? "black" : "grey.300",
                                padding: 2,
                            }}
                        >
                            <FormControlLabel
                                value={addr.addressId.toString()}
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
                                            <Button size="small" variant="outlined"
                                            onClick={() => deleteAddress(addr.addressId)}>
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

            <Button onClick={completeSubmit} variant="contained" color="inherit" fullWidth sx={{ mt: 3 }}>
                변경하기
            </Button>
        </Box>
    )
}
