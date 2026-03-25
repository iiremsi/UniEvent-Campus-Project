import { useState , useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; 
import { postAPI } from '../services/UserService';
import { AiOutlineUser , AiTwotoneHome , AiOutlineLogout , AiFillQqCircle} from "react-icons/ai";
import PostCard from '../components/PostCard';

export default function Home() {
  
  const navigate = useNavigate();  
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        setLoading(true);
        const response = await postAPI.getAll();
        console.log('Posts:', response.data);
        setPosts(response.data.content);
        setError(null);
      } catch (err) {
        console.error('Error:', err);
        setError('Post yüklenemedi');
      } finally {
        setLoading(false);
      }
    };
    fetchPosts();
  }, []);

  const handleLogout = () => {
    const confirmLogout = window.confirm('Are you sure you wanna logout?');
    
    if (confirmLogout) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      navigate('/');
      
      alert('Logged out succesfully!');
    }
  };

  const profile = () => {
    navigate('/profile')
  };

  
  return (
    <div className="flex min-h-screen max-w-full mx-auto border-x border-gray-700 bg-gray-800 text-white">

      {/* 1. SIDEBAR - Arka plan bg-gray-900 olarak güncellendi */}
      <aside className="hidden lg:flex flex-col w-72 p-6 bg-gray-900 border-r border-gray-700 gap-8 min-h-screen sticky top-0">
        <div className="flex items-center justify-center p-10 pb-15 rounded-lg">
          <AiFillQqCircle className='size-16 text-blue-500'/>
        </div>
        <div className="flex items-center gap-3 p-4 text-gray-300 hover:bg-gray-800 rounded-lg cursor-pointer transition">
          <AiTwotoneHome className='size-7'/>
          <div>
            <p className="font-semibold">Home Page</p>
          </div>
        </div>
        <div onClick={profile} className="flex items-center gap-3 p-4 text-gray-300 hover:bg-gray-800 rounded-lg cursor-pointer transition">
          <AiOutlineUser className='size-7'/>
          <div>
            <p className="font-semibold">Profile</p>
          </div>
        </div>
        <div onClick={handleLogout} className="flex items-center gap-3 p-4 text-gray-300 hover:bg-gray-800 rounded-lg cursor-pointer transition">
          <AiOutlineLogout className='size-7'/>
          <div>
            <p className="font-semibold">Log Out</p>
          </div>
        </div>
      </aside>

      {/* 2. ANA SÜTUN */}
      <main className="flex-1 border-r border-gray-700">
        {/* HEADER - Arka plan bg-gray-900 yapıldı */}
        <header className="p-4 border-b border-gray-700 sticky top-0 bg-gray-900/95 backdrop-blur-sm flex items-center justify-between z-10">
          <h2 className="text-xl font-bold text-white">Home Page</h2>

          <button
            onClick={() => navigate('/create-post')}
            className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition font-bold"
          >
            ➕ New Post
          </button>
        </header>

        <div className="flex flex-col items-center p-4">
          <div className="w-full max-w-6xl space-y-6"> 
            
            {loading && (
              <div className="p-8 text-center text-gray-500 italic">
                Yükleniyor...
              </div>
            )}

            {error && (
              <div className="p-4 bg-red-900/20 text-red-400 m-4 rounded border border-red-900/30 text-center">
                {error}
              </div>
            )}

            {!loading && !error && posts.length === 0 && (
              <div className="p-8 text-center text-gray-500">
                Henüz gönderi yok
              </div>
            )}

            {!loading && !error && posts.length > 0 && (
              <div className="space-y-4">
                {posts.map(post => (
                  <PostCard key={post.id} post={post} />
                ))}
              </div>
            )}
          </div>
        </div>
      </main>

    </div>
  );
}