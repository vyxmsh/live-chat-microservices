'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/src/components/Sidebar';
import { useHeartbeat } from '@/src/hooks/usePresence';

export default function AppLayout({ children }: { children: React.ReactNode}){
    const router = useRouter();

    //Guard: redirect to login if no token
    useEffect(() => {
        const token = localStorage.getItem('token');
        if(!token){
            router.push('/login');
        }
    },[router]);

    //Keep current user online while the app is open
    useHeartbeat();

    return (
        <div className="flex h-screen bg-gray-900 overflow-hidden">
            <Sidebar />
            <main className="flex-1 overflow-hidden">
                {children}
            </main>
        </div>
    );
    
}