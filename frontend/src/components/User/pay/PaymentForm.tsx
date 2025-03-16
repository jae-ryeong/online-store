import { useEffect, useState } from "react";
import "./css/payment.css";
import { useNavigate } from "react-router-dom";
import PortOne from "@portone/browser-sdk/v2";
import Header from "../../header/TotalHeader";

// 타입 정의
interface OrderItem {
    id: string;
    itemName: string;
    price: number;
    quantity: number;
    options?: string[];
}
interface DeliveryInfo {
    receiverName: string;
    phoneNumber: string;
    address: string;
    detailAddress: string;
    deliveryRequest?: string;
}
interface PaymentRequest {
    amount: number;
    orderName: string;
    buyerName: string;
    buyerEmail: string;
}
interface PaymentStatus {
    status: "IDLE" | "PENDING" | "PAID" | "FAILED";
    message?: string;
}

export default function PaymentForm() {
    const nav = useNavigate();
    const [deliveryInfo, setDeliveryInfo] = useState<DeliveryInfo>({
        receiverName: "",
        phoneNumber: "",
        address: "",
        detailAddress: "",
    });
    const [agreeTerms, setAgreeTerms] = useState(false);
    const [formData, setFormData] = useState({
        name: "",
        phone: "",
        address: "",
        email: ""
    });
    const [orderItems, setOrderItems] = useState<OrderItem[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const [paymentStatus, setPaymentStatus] = useState<PaymentStatus>({
        status: "IDLE",
    });

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

        const fetchCartData = async () => {
            try {
                const response = await fetch("http://localhost:8080/api/v1/order/list/orderitem", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    setOrderItems(data);
                }
            } catch (error) {
                console.error("order/list/orderitem API 호출 에러:", error);
            }
        }
        fetchCartData();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const totalAmount = orderItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    // 결제 함수
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

    // 결제 실패 시 order 삭제 함수
    const deleteOrder = async (orderId: number) => {
        console.log("주문 삭제 실행");
        const token = getAuth();
        const response = await fetch(`http://localhost:8080/api/v1/order/deleteorder/${orderId}`, {
            method: "DELETE",
            body: JSON.stringify({
                orderId
            }),
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        if (response.ok) {
            console.log("주문 삭제 성공");
        } else {
            console.error("주문 삭제 실패");
        }
    }

    // 결제 성공 시 itemCart 삭제 및 quantity 감소 함수
    const successOrder = async (orderId: number) => {
        console.log("주문 성공 실행");
        const token = getAuth();
        const response = await fetch(`http://localhost:8080/api/v1/order/successorder/${orderId}`, {
            method: "PUT",
            body: JSON.stringify({
                orderId
            }),
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        if (response.ok) {
            console.log("주문 성공 성공");
        } else {
            console.error("주문 성공 실패");
        }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
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
                    phoneNumber: formData.phone,
                }
            });

            if (response?.code !== undefined) {
                setPaymentStatus({
                    status: "FAILED",
                    message: response.message,
                });
                // order 삭제 메소드
                deleteOrder(order.orderId);
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
                successOrder(order.orderId);
            } else {
                setPaymentStatus({
                    status: "FAILED",
                    message: await completeResponse.text(),
                });
                // order 삭제 메소드
                deleteOrder(order.orderId);
            }
        } catch (error) {
            console.error("결제 검증 실패: ", error);
            deleteOrder(order.orderId);
        }
    };

    const isWaitingPayment = paymentStatus.status !== "IDLE";

    const handleFailClose = () => {
        setPaymentStatus({ status: "FAILED" });
        nav("/mypage/cart");
    }
    const handleSuccessClose = () => {
        setPaymentStatus({ status: "IDLE" });
        nav("/mypage/orderlist");
    }

    return (
        <div className="totalWrapper">
            <Header />
            <div className="payment-container">                
                <h2 className="payment-title">결제 정보 입력</h2>

                <form onSubmit={handleSubmit} className="payment-form">
                    <div className="form-columns">
                        <div className="payment-section">
                            <h3>구매자 정보</h3>

                            <div className="form-group">
                                <label>이름</label>
                                <input className="input-style"
                                    type="text" name="name"
                                    value={formData.name} onChange={handleChange} required />
                            </div>

                            <div className="form-group">
                                <label>이메일</label>
                                <input className="input-style"
                                    type="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange} required />
                            </div>

                            <div className="form-group">
                                <label>전화번호</label>
                                <input className="input-style"
                                    type="tel"
                                    name="phone"
                                    value={formData.phone}
                                    onChange={handleChange} required />
                            </div>
                        </div>

                        <div className="summary-section">
                            <div className="section-box">
                                <h3 className="section-title">주문 내역</h3>



                                {orderItems.map((item, index) => (
                                    <div key={index} className="product-item">
                                        <div>
                                            <div className="product-name">{item.itemName}, {item.quantity}개</div>
                                            <div className="product-price">
                                                {item.price * item.quantity}원
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>

                            <div className="total-amount">
                                <span>총 결제 금액: {totalAmount.toLocaleString()}원</span>
                            </div>

                            <div className="agree-terms">
                                <label>
                                    <input
                                        className="checkbox-style"
                                        type="checkbox"
                                        checked={agreeTerms}
                                        onChange={(e) => setAgreeTerms(e.target.checked)}
                                    />
                                    주문 내용을 확인하였으며, 결제에 동의합니다.
                                </label>
                            </div>

                            {error && <div className="errorStyle">{error}</div>}

                            <button type="submit" className="pay-button" aria-busy={isWaitingPayment}
                                disabled={!agreeTerms || isWaitingPayment}>결제하기</button>
                        </div>
                    </div>
                </form>

                {paymentStatus.status === "FAILED" && (
                    <dialog open>
                        <header>
                            <h1>결제 실패</h1>
                        </header>
                        <p>{paymentStatus.message}</p>
                        <button type="button" onClick={handleFailClose}>
                            닫기
                        </button>
                    </dialog>
                )}
                <dialog open={paymentStatus.status === "PAID"}>
                    <header>
                        <h1>결제 성공</h1>
                    </header>
                    <p>결제에 성공했습니다.</p>
                    <button type="button" onClick={handleSuccessClose}>
                        닫기
                    </button>
                </dialog>
            </div>
        </div>
    )
}