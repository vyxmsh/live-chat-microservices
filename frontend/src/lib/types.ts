export interface User {
    id: number;
    username: string;
}

export interface Channel{
    id: number;
    name: string;
    description?: string;
    createdAt: string;
}

export interface Message{
    id: number;
    channelId: number;
    senderId: number;
    senderName: string;
    content: string;
    createdAt: string;
}

export interface PagedMessage{
    messages: Message[];
    currentPage: number;
    totalPages: number;
    hasMore: boolean;
}

export interface AuthResponse{
    token: string;
    username: string;
}

export interface Notification{
    id: number;
    userId: number;
    type: string;
    content: string;
    read: boolean;
    createdAt: string;
}