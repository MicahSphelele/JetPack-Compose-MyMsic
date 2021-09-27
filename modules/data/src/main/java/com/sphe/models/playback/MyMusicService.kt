package com.sphe.models.playback

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.media.MediaBrowserServiceCompat
import com.sphe.base.base.CoroutineDispatchers
import com.sphe.models.MediaID
import com.sphe.models.MediaID.Companion.CALLER_SELF
import com.sphe.models.R
import com.sphe.models.constants.Utils.EMPTY_ALBUM_ART_URI
import com.sphe.models.db.QueueHelper
import com.sphe.models.db.entities.QueueEntity
import com.sphe.models.extension.toIDList
import com.sphe.models.extension.toRawMediaItems
import com.sphe.models.notifications.Notifications
import com.sphe.models.repositories.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MyMusicService : MediaBrowserServiceCompat(), LifecycleOwner {

    companion object {
        const val MEDIA_ID_ARG = "MEDIA_ID"
        const val MEDIA_TYPE_ARG = "MEDIA_TYPE"
        const val MEDIA_CALLER = "MEDIA_CALLER"
        const val MEDIA_ID_ROOT = -1
        const val TYPE_ALL_ARTISTS = 0
        const val TYPE_ALL_ALBUMS = 1
        const val TYPE_ALL_SONGS = 2
        const val TYPE_ALL_PLAYLISTS = 3
        const val TYPE_SONG = 9
        const val TYPE_ALBUM = 10
        const val TYPE_ARTIST = 11
        const val TYPE_PLAYLIST = 12
        const val TYPE_ALL_FOLDERS = 13
        const val TYPE_ALL_GENRES = 14
        const val TYPE_GENRE = 15
        const val NOTIFICATION_ID = 888
    }

    @Inject
    protected lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var notifications: Notifications

    @Inject
    lateinit var albumRepository: AlbumRepository

    @Inject
    lateinit var artistRepository: ArtistRepository

    @Inject
    lateinit var songsRepository: SongsRepository

    @Inject
    lateinit var genreRepository: GenreRepository

    @Inject
    lateinit var playlistRepository: PlaylistRepository

    @Inject
    lateinit var player: SongPlayer

    @Inject
    lateinit var queueHelper: QueueHelper

    private lateinit var becomingNoisyReceiver: BecomingNoisyReceiver

    private val lifecycle = LifecycleRegistry(this)

    @DelicateCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        //TODO(You need to consider storage permissions)
        GlobalScope.launch {
            player.setQueue()
        }

        sessionToken = player.getSession().sessionToken
        sessionToken?.let {
            becomingNoisyReceiver = BecomingNoisyReceiver(this, it)
        }

        player.onPlayingState { isPlaying ->
            if (isPlaying) {
                becomingNoisyReceiver.register()
                startForeground(NOTIFICATION_ID, notifications.buildNotification(getSession()))
            } else {
                becomingNoisyReceiver.unregister()
                stopForeground(false)
                saveCurrentData()
            }
            notifications.updateNotification(player.getSession())
        }

        player.onCompletion {
            notifications.updateNotification(player.getSession())
        }
    }


    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    override fun getLifecycle(): Lifecycle  = lifecycle

    @DelicateCoroutinesApi
    private fun saveCurrentData() {
        GlobalScope.launch(Dispatchers.IO) {
            val mediaSession = player.getSession()
            val controller = mediaSession.controller
            if (controller == null ||
                controller.playbackState == null ||
                controller.playbackState.state == PlaybackStateCompat.STATE_NONE
            ) {
                return@launch
            }

            val queue = controller.queue
            val currentId = controller.metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
            queueHelper.updateQueueSongs(queue?.toIDList(), currentId?.toLong())

            val queueEntity = QueueEntity().apply {
                this.currentId = currentId?.toLong()
                currentSeekPos = controller.playbackState?.position
                repeatMode = controller.repeatMode
                shuffleMode = controller.shuffleMode
                playState = controller.playbackState?.state
                queueTitle = controller.queueTitle?.toString() ?: getString(R.string.all_songs)
            }
            queueHelper.updateQueueData(queueEntity)
        }
    }

    private fun addMediaRoots(mMediaRoot: MutableList<MediaBrowserCompat.MediaItem>, caller: String) {
        mMediaRoot.add(MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().apply {
                setMediaId(MediaID(TYPE_ALL_ARTISTS.toString(), null, caller).asString())
                setTitle(getString(R.string.artists))
                setIconUri(EMPTY_ALBUM_ART_URI.toUri())
                setSubtitle(getString(R.string.artists))
            }.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        ))

        mMediaRoot.add(MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().apply {
                setMediaId(MediaID(TYPE_ALL_ALBUMS.toString(), null, caller).asString())
                setTitle(getString(R.string.albums))
                setIconUri(EMPTY_ALBUM_ART_URI.toUri())
                setSubtitle(getString(R.string.albums))
            }.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        ))

        mMediaRoot.add(MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().apply {
                setMediaId(MediaID(TYPE_ALL_SONGS.toString(), null, caller).asString())
                setTitle(getString(R.string.songs))
                setIconUri(EMPTY_ALBUM_ART_URI.toUri())
                setSubtitle(getString(R.string.songs))
            }.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        ))

        mMediaRoot.add(MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().apply {
                setMediaId(MediaID(TYPE_ALL_PLAYLISTS.toString(), null, caller).asString())
                setTitle(getString(R.string.playlists))
                setIconUri(EMPTY_ALBUM_ART_URI.toUri())
                setSubtitle(getString(R.string.playlists))
            }.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        ))

        mMediaRoot.add(MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().apply {
                setMediaId(MediaID(TYPE_ALL_GENRES.toString(), null, caller).asString())
                setTitle(getString(R.string.genres))
                setIconUri(EMPTY_ALBUM_ART_URI.toUri())
                setSubtitle(getString(R.string.genres))
            }.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        ))
    }

    private fun loadChildren(parentId: String): ArrayList<MediaBrowserCompat.MediaItem> {
        val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
        val mediaIdParent = MediaID().fromString(parentId)

        val mediaType = mediaIdParent.type
        val mediaId = mediaIdParent.mediaId
        val caller = mediaIdParent.caller

        if (mediaType == MEDIA_ID_ROOT.toString()) {
            addMediaRoots(mediaItems, caller!!)
        } else {
            when (mediaType?.toInt() ?: 0) {
                TYPE_ALL_ARTISTS -> {
                    mediaItems.addAll(artistRepository.getAllArtists(caller))
                }
                TYPE_ALL_ALBUMS -> {
                    mediaItems.addAll(albumRepository.getAllAlbums(caller))
                }
                TYPE_ALL_SONGS -> {
                    mediaItems.addAll(songsRepository.loadSongs(caller))
                }
                TYPE_ALL_GENRES -> {
                    mediaItems.addAll(genreRepository.getAllGenres(caller))
                }
                TYPE_ALL_PLAYLISTS -> {
                    mediaItems.addAll(playlistRepository.getPlaylists(caller))
                }
                TYPE_ALBUM -> {
                    mediaId?.let {
                        mediaItems.addAll(albumRepository.getSongsForAlbum(it.toLong(), caller))
                    }
                }
                TYPE_ARTIST -> {
                    mediaId?.let {
                        mediaItems.addAll(artistRepository.getSongsForArtist(it.toLong(), caller))
                    }
                }
                TYPE_PLAYLIST -> {
                    mediaId?.let {
                        mediaItems.addAll(playlistRepository.getSongsInPlaylist(it.toLong(), caller))
                    }
                }
                TYPE_GENRE -> {
                    mediaId?.let {
                        mediaItems.addAll(genreRepository.getSongsForGenre(it.toLong(), caller))
                    }
                }
            }
        }

        return if (caller == CALLER_SELF) {
            mediaItems
        } else {
            mediaItems.toRawMediaItems()
        }
    }
}