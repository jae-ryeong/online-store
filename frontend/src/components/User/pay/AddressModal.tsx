import { Box, Button, FormControlLabel, Paper, Radio, RadioGroup, Stack, Typography } from "@mui/material";
import { useState } from "react";

interface Address {
    id: number;
    receiverName: string;
    address: string;
    detailAddress: string;
    tel: string;
    isDefault?: boolean;
}

export default function AddressModal() {
    const [selectedId, setSelectedId] = useState<number>(4);

    const addresses: Address[] = [
        {
            id: 1,
            receiverName: "김재령",
            address: "대전 동구 동산초교로 46 (신동아파밀리에아파트1단지)",
            detailAddress: "101동 1001호",
            tel: "010-7610-6751",
            
        },
        {
            id: 2,
            receiverName: "남현숙",
            address: "대전광역시 서구 갈마로 163",
            detailAddress: "대한주택종합관리(주)2층",
            tel: "010-3657-5147",
            isDefault: true,
        },
        {
            id: 3,
            receiverName: "김재령",
            address: "충청북도 충주시 대소원면 검단리 67-3",
            detailAddress: "대성빌라 303호",
            tel: "010-8387-0297",
        },
        {
            id: 4,
            receiverName: "김재령",
            address: "충청북도 충주시 대소원면 검단리 71-2",
            detailAddress: "하늘정원 202호",
            tel: "010-7610-6751",
        },
    ];

    const handleSubmit = () => {
        const selected = addresses.find((addr) => addr.id === selectedId);
        if(selected && window.opener) {
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
                                            {addr.receiverName}{" "}
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