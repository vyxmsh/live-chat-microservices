'use Client';

import { useEffect,useRef,useState, useCallback } from "react";
import api from '../lib/api';
import { Message, PagedMessage } from '../lib/types'; 
import { useWebSocket } from "../hooks/useWebSocket";
import MessageInput from "./MessageInput";

interface MessageFeedProps {
    channelId: number;
    channelName: string;
}

export default function MessageFeed({channelId, channelName}: MessageFeedProps){
    const [messages, setMessages] = useState<Message[]>([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [loadingHistory, setLoadingHistory] = useState(false);
    const feedRef = useRef<HTMLDivElement>(null);
    const bottomRef = useRef<HTMLDivElement>(null);
    const isFirstLoad = useRef(true);

    const username = typeof window !== 'undefined'
    ? localStorage.getItem('username') ?? '':'';

    const userId = typeof window !== 'undefined'
    ? Number(localStorage.getItem('userId')):0;

    //load a page of message history
    const loadMessage = useCallback(async (pageNum: number) => {
        if(loadingHistory) return;
        setLoadingHistory(true);
        try {
            const res = await api.get<PagedMessage>(
                `/api/channels/${channelId}/messages?pages=${pageNum}`
            );
        const reversed = [...res.data.messages].reverse();//oldest first
        setMessages((prev) => [...reversed, ...prev]);
        setHasMore(res.data.hasMore);
        } catch (err) {
            console.error('Failed to load message:', err);
        } finally {
            setLoadingHistory(false);
        }
    }, [channelId, loadingHistory]);

    //reset and load when channel changes
    useEffect(() => {
        setMessages([]);
        setPage(0);
        setHasMore(true);
        isFirstLoad.current = true;
        loadMessage(0);
    },[channelId]);

    //scroll to bottom on first load only
    useEffect(() => {
        if(isFirstLoad.current && messages.length>0){
            bottomRef.current?.scrollIntoView();
            isFirstLoad.current = false;
        }
    }, [messages]);

    //infine scroll upward - load more when scrolled to top
    const handleScroll = ()=> {
        if(!feedRef.current) return;
        if (feedRef.current.scrollTop === 0 && hasMore && !loadingHistory)
        {
            const nextPage = page + 1;
            setPage(nextPage);
            loadMessage(nextPage);
        }
    };

    //New real-time message via websocket
    const handleNewMessage = useCallback((message: Message) => {
        setMessages((prev) =>[...prev, message]);
        //scroll to bottom when a new message arrives
        setTimeout(() => bottomRef.current?.scrollIntoView({behavior: 'smooth'}),50);
    }, []);

    const { connected, sendMessage} = useWebSocket ({
        channelId,
        onMessage: handleNewMessage,
    });

    const handleSend = (content: string) => {
        sendMessage({
            channelId,
            senderId: userId,
            senderName: username,
            content,
        });
    };

    return (
    <div className="flex flex-col h-full bg-gray-900">
      {/* Header */}
      <div className="px-6 py-4 border-b border-gray-700 flex items-center gap-3">
        <span className="text-white font-semibold text-lg"># {channelName}</span>
        <span
          className={`text-xs px-2 py-0.5 rounded-full ${
            connected
              ? 'bg-green-900 text-green-400'
              : 'bg-yellow-900 text-yellow-400'
          }`}
        >
          {connected ? 'Live' : 'Connecting...'}
        </span>
      </div>

      {/* Message list */}
      <div
        ref={feedRef}
        onScroll={handleScroll}
        className="flex-1 overflow-y-auto px-6 py-4 space-y-3"
      >
        {loadingHistory && (
          <div className="text-center text-gray-500 text-sm py-2">
            Loading older messages...
          </div>
        )}

        {!hasMore && messages.length > 0 && (
          <div className="text-center text-gray-600 text-xs py-2">
            Beginning of channel
          </div>
        )}

        {messages.map((msg) => (
          <div key={msg.id} className="flex flex-col gap-0.5">
            <div className="flex items-baseline gap-2">
              <span className="text-indigo-400 font-semibold text-sm">
                {msg.senderName}
              </span>
              <span className="text-gray-600 text-xs">
                {new Date(msg.createdAt).toLocaleTimeString([], {
                  hour: '2-digit',
                  minute: '2-digit',
                })}
              </span>
            </div>
            <p className="text-gray-200 text-sm leading-relaxed">{msg.content}</p>
          </div>
        ))}

        <div ref={bottomRef} />
      </div>

      {/* Input */}
      <MessageInput onSend={handleSend} disabled={!connected} />
    </div>
  );

}