import { useEffect, useRef, useState} from 'react';
import { Client, IMessage} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Message } from '../lib/types';

interface UseWebSocketOptions {
    channelId: number;
    onMessage: (message:Message) => void;
}

export function useWebSocket({ channelId, onMessage}: UseWebSocketOptions){
    const clientRef = useRef<Client | null>(null);
    const [connected,setConnected] = useState(false);

    useEffect(() => {
        const token = localStorage.item('token');
        if(!token || !channelId) return;

        const client = new Client ({
            webSocketFactory: () => 
                new SockJS('${process.env.NEXT_PUBLIC_API_URL}/ws'),

            connectHeaders:{
                Authorization: 'Bearer ${token}',

            },

            onConnect: () => {
                setConnected(true);

                //subscribe to this channel's topic
                client.subscribe('/topic/channel.${channelId}',
                    (frame: IMessage) => {
                        const message: Message = JSON.parse(frame.body);
                        onMessage(message);
                    }
                );
            },

            onDisconnect: () => {
                setConnected(false);
            },

            onStompError: (frame) => {
                console.error('STOMP error:', frame);
                setConnected(false);
            },

            reconnectDelay: 5000,
        });

        client.activate();
        clientRef.current = client;

        //cleanup on unamount or channelId change
        return() => {
            client.deactivate();
            clientRef.current = null;
            setConnected(false);
        };
    }, [channelId]); //re-run when channel changes

    //send a message via WebSocket
    const sendMessage = (payload: object) => {
        if(clientRef.current?.connected) {
            clientRef.current.publish({
                destination: '/app/chat.send',
                body: JSON.stringify(payload),
            });
        }
    };

    return {connected, sendMessage};
}