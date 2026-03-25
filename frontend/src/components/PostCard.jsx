import { useState } from 'react';  
import { postAPI } from '../services/UserService';

export default function PostCard({ post }) {

  const [likeCount, setLikeCount] = useState(post.likeCount || 0);
  
  const [isLiked, setIsLiked] = useState(false);
  
  const [loading, setLoading] = useState(false);


  const handleLike = async () => {
  
    if (loading) return;
    
    try {
      setLoading(true);  
      
      if (isLiked) {
        
        await postAPI.unlike(post.id);
        setLikeCount(likeCount - 1);  
        setIsLiked(false);             
      } else {
        await postAPI.like(post.id);
        setLikeCount(likeCount + 1);  
        setIsLiked(true);              
      }
      
    } catch (err) {
      console.error('❌ Like hatası:', err);
      alert('Like işlemi başarısız!');
    } finally {
      setLoading(false);  
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6 mb-4">
      
      <div className="flex items-center gap-3 mb-4">
        <div className="w-12 h-12 bg-blue-500 rounded-full flex items-center justify-center text-white font-bold">
          {post.authorDisplayName?.charAt(0) || 'C'}
        </div>
        <div>
          <p className="font-bold text-black">{post.authorDisplayName}</p>
          <p className="text-sm text-gray-500">
            @{post.authorUsername} · {new Date(post.createdAt).toLocaleDateString('tr-TR')}
          </p>
        </div>
      </div>

      <p className="text-gray-800 mb-4">
        {post.content}
      </p>

      {post.eventTitle && (
        <div className="bg-blue-100 rounded-lg p-4 mb-4">
          <p className="font-semibold text-blue-900">{post.eventTitle}</p>
          {post.eventLocation && (
            <p className="text-sm text-blue-700">📍 {post.eventLocation}</p>
          )}
          {post.eventDate && (
            <p className="text-sm text-blue-700">
              📅 {new Date(post.eventDate).toLocaleString('tr-TR')}
            </p>
          )}
        </div>
      )}

      {post.imageUrl && (
        <div className="mb-4">
          <img 
            src={post.imageUrl} 
            alt="Post" 
            className="w-full h-auto max-h-175 object-cover object-center"
          />
        </div>
      )}

      <div className="flex items-center gap-6">
        <button 
          onClick={handleLike}
          disabled={loading}
          className={`flex items-center gap-2 transition ${
            isLiked 
              ? 'text-red-500' 
              : 'text-gray-600 hover:text-red-500'
          } ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
        >
          <span>{isLiked ? '❤️' : '🤍'}</span>
          <span>{likeCount} likes</span>
        </button>
      </div>

    </div>
  );
}