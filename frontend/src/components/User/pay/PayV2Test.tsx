import PortOne from "@portone/browser-sdk/v2";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Item {
    id: string;
    name: string;
    price: number;
    currency: any;
}

interface PaymentStatus {
    status: "IDLE" | "PENDING" | "PAID" | "FAILED";
    message?: string;
}

interface FormData {
    name: string;
    phone: string;
    email: string;
}

interface OrderItem {
    itemName: string;
    quantity: number;
    itemPrice: number;
}

interface PaymentRequest {
    amount: number;
    orderName: string;
    buyerName: string;
    buyerEmail: string;
}

export function PayV2Test() {
    const nav = useNavigate();
    const [paymentStatus, setPaymentStatus] = useState<PaymentStatus>({
        status: "IDLE",
    });

    const [formData, setFormData] = useState<FormData>({
        name: "name",
        phone: "01076106751",
        email: "wofud0321@naver.com",
    });
    const [submitted, setSubmitted] = useState(false);
    const [orderItems] = useState<OrderItem[]>([
        { itemName: "아이폰 13 Pro", quantity: 1, itemPrice: 1000 },
        { itemName: "아이폰 13 Pro Max", quantity: 2, itemPrice: 500 },
    ]);
    const getAuth = () => {
        const token = localStorage.getItem("accessToken")

        if (!token) {
            console.error("JWT 토큰이 없습니다. 로그인 해주세요.");
            nav("/category/home");   // 로그인 되어 있지 않을때 메인페이지로 이동
            return;
        }
        return token;
    }

    // 총액 계산
    const totalAmount = orderItems.reduce((sum, item) => sum + (item.itemPrice * item.quantity), 0);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const createOrder = async () => {
        const token = getAuth();
        const response = await fetch("http://localhost:8080/api/v1/order/createorder", {
            method: "POST",
            body: JSON.stringify({
                amount: totalAmount,
                itemName: orderItems.length > 1 ? `${orderItems[0].itemName} 외 ${orderItems.length - 1}건` : orderItems[0].itemName,
            }),
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });
        const order = await response.json();
        return order;
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setSubmitted(true);
        const order = await createOrder();

        try {
            // randomId 생성
            function randomId(): string {
                return [...crypto.getRandomValues(new Uint32Array(2))]
                    .map((word) => word.toString(16).padStart(8, "0"))
                    .join("");
            }
            console.log("PaymentRquest 실행 차례")
            const paymentReqeust: PaymentRequest = {
                amount: totalAmount,
                orderName: orderItems.length > 1 ? `${orderItems[0].itemName} 외 ${orderItems.length - 1}건` : orderItems[0].itemName,
                buyerName: formData.name,
                buyerEmail: formData.email,
            };
            const paymentId = randomId();
            console.log("paymentId: ", paymentId);
            const response = await PortOne.requestPayment({
                storeId: process.env.REACT_APP_PORTONE_STORE_ID as string,
                channelKey: process.env.REACT_APP_PORTONE_CHANNEL_KEY as string,
                paymentId,
                orderName: paymentReqeust.orderName,
                totalAmount: paymentReqeust.amount,
                currency: "CURRENCY_KRW",
                payMethod: "CARD",
                customer: {
                    email: paymentReqeust.buyerEmail,
                    fullName: paymentReqeust.buyerName,
                    phoneNumber: formData.phone,}
            });

            if (response?.code !== undefined) {
                setPaymentStatus({
                    status: "FAILED",
                    message: response.message,
                });
                return;
            }
            const completeResponse = await fetch("/api/v1/pay/verify", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    paymentId: paymentId,
                    amount: paymentReqeust.amount,
                    orderId: order.orderId
                }),
            });

            if (completeResponse.ok) {
                setPaymentStatus({ status: "PAID" });
            } else {
                setPaymentStatus({
                    status: "FAILED",
                    message: await completeResponse.text(),
                });
            }
        } catch (error) {
            console.error("결제 검증 실패: ", error);
        }
    };

    const isWaitingPayment = paymentStatus.status !== "IDLE";

    const handleClose = () => setPaymentStatus({ status: "IDLE" });

    return (
        <>
            <main>
                <form onSubmit={handleSubmit}>
                    <article>
                        <div className="order-items">
                            {orderItems.map((item, index) => (
                                <div key={index} className="order-item">
                                    <span>{item.itemName}</span>
                                    <span>{item.quantity}개</span>
                                    <span>{item.itemPrice.toLocaleString()}원</span>
                                </div>
                            ))}
                        </div>

                        <div className="total-amount">
                            <label>총 결제 금액:</label>
                            {totalAmount.toLocaleString()}원
                        </div>
                    </article>
                    <button type="submit" aria-busy={isWaitingPayment} disabled={isWaitingPayment} className="pay-button">
                        결제
                    </button>
                </form>
            </main>


            {paymentStatus.status === "FAILED" && (
                <dialog open>
                    <header>
                        <h1>결제 실패</h1>
                    </header>
                    <p>{paymentStatus.message}</p>
                    <button type="button" onClick={handleClose}>
                        닫기
                    </button>
                </dialog>
            )}
            <dialog open={paymentStatus.status === "PAID"}>
                <header>
                    <h1>결제 성공</h1>
                </header>
                <p>결제에 성공했습니다.</p>
                <button type="button" onClick={handleClose}>
                    닫기
                </button>
            </dialog>
        </>
    );
}
