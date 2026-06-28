'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import api from '../../../../lib/api';
import { Channel } from '../../../../lib/types';
import MessageFeed from '@/src/components/MessageFeed';

export default function ChannelPage() {
    const param = useParams();
    const channelId = Number(param.id);
    const [channel, setChannel] = useState<Channel | null>(null);

    useEffect(()=>{
        if(!channelId) return;
        api.get<Channel>(`/api/channels/${channelId}`)
           .then((r) => setChannel(r.data))
           .catch(console.error);
    }, [channelId]);

    if(!channel){
        return(
            <div className="flex items-center justify-center h-full text-gray-500">
                Loading channel...
            </div>
        );
    }

    return (
        <MessageFeed
          channelId={channel.id}
          channelName={channel.name} 
          />
    );
}