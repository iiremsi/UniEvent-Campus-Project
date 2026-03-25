import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { postAPI } from '../services/UserService';
import Input from '../components/Input';
import Button from '../components/Button';

export default function CreatePost() {
  const [formData, setFormData] = useState({
    content: '',
    eventTitle: '',
    eventLocation: '',
    eventDate: '',
    imageUrl: ''
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.content.trim()) {
      alert('Post içeriği boş olamaz!');
      return;
    }

    try {
      setLoading(true);
      
      // Tarih formatını düzelt (YYYY-MM-DDTHH:MM formatından ISO formatına)
      const postData = {
        ...formData,
        eventDate: formData.eventDate ? new Date(formData.eventDate).toISOString() : null,
        imageUrl: formData.imageUrl || null
      };
      
      await postAPI.create(postData);
      
      alert('✅ Post created!');
      navigate('/home');
      
    } catch (err) {
      console.error('❌ Hata:', err);
      alert('❌ Post oluşturulamadı: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
  <div className="min-h-screen bg-gray-950 text-white flex justify-center p-6 pt-10">
    {/* Ana Kart - Diğer sayfalarla uyumlu bg-gray-900 */}
    <div className="w-full max-w-2xl bg-gray-900 rounded-2xl shadow-2xl border border-gray-800 p-8 h-fit">
      <h1 className="text-2xl font-black mb-8 tracking-tight text-center">CREATE NEW EVENT</h1>
      
      <form onSubmit={handleSubmit} className="space-y-6">
        
        {/* Post İçeriği */}
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2 ml-1">
            Event Explanation 
          </label>
          <textarea
            className="w-full p-4 bg-gray-800 border border-gray-700 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition text-white placeholder-gray-500"
            rows="4"
            placeholder="What's on your mind?"
            value={formData.content}
            onChange={(e) => setFormData({...formData, content: e.target.value})}
            required
          />
        </div>

        {/* Etkinlik Başlığı */}
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2 ml-1">
            Event Name
          </label>
          <Input
            type="text"
            maxLength="100"
            className="w-full bg-gray-800 border-gray-700 text-white rounded-xl focus:ring-2 focus:ring-blue-500"
            placeholder="e.g. Tech Workshop"
            value={formData.eventTitle}
            onChange={(e) => setFormData({...formData, eventTitle: e.target.value})}
          />
        </div>

        {/* Etkinlik Yeri */}
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2 ml-1">
            Place 
          </label>
          <Input
            type="text"
            maxLength="150"
            className="w-full bg-gray-800 border-gray-700 text-white rounded-xl focus:ring-2 focus:ring-blue-500"
            placeholder="e.g. Main Hall"
            value={formData.eventLocation}
            onChange={(e) => setFormData({...formData, eventLocation: e.target.value})}
          />
        </div>

        {/* Etkinlik Tarihi */}
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2 ml-1">
            Date
          </label>
          <div className="flex justify-center">
            <input
              type="datetime-local"
              className="w-full p-3 bg-gray-800 border border-gray-700 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200 font-semibold text-white color-scheme-dark"
              style={{ colorScheme: 'dark' }} // Tarih ikonunun beyaz görünmesi için
              value={formData.eventDate}
              onChange={(e) => setFormData({...formData, eventDate: e.target.value})}
            />
          </div>
        </div>

        {/* Fotoğraf URL */}
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2 ml-1">
            Photo URL
          </label>
          <Input
            type="url"
            className="w-full bg-gray-800 border-gray-700 text-white rounded-xl focus:ring-2 focus:ring-blue-500"
            placeholder="https://example.com/image.jpg"
            value={formData.imageUrl}
            onChange={(e) => setFormData({...formData, imageUrl: e.target.value})}
          />
          {formData.imageUrl && (
            <div className="mt-4 p-2 bg-gray-950 rounded-xl border border-gray-800">
              <p className="text-xs text-gray-500 mb-2 ml-1">Preview:</p>
              <img 
                src={formData.imageUrl} 
                alt="Preview" 
                className="w-full max-h-48 object-cover rounded-lg"
                onError={(e) => {
                  e.target.style.display = 'none';
                }}
              />
            </div>
          )}
        </div>

        {/* Butonlar */}
        <div className="flex gap-4 pt-4">
          <div className="flex-1">
            <Button 
              text={loading ? "Creating..." : "Share Event"}
              onClick={handleSubmit}
              disabled={loading}
              className="w-full py-4 bg-blue-600 hover:bg-blue-700 rounded-xl font-bold transition shadow-lg shadow-blue-500/20"
            />
          </div>
          <button
            type="button"
            onClick={() => navigate('/home')}
            className="px-8 py-2 bg-gray-800 text-gray-400 rounded-xl hover:bg-gray-700 hover:text-white transition font-bold"
          >
            Cancel
          </button>
        </div>

      </form>
    </div>
  </div>
);
}