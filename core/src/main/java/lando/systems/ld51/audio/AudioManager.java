package lando.systems.ld51.audio;

import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld51.Main;
import lando.systems.ld51.assets.Assets;

public class AudioManager implements Disposable {

    public MutableFloat soundVolume;
    public MutableFloat musicVolume;

    public static boolean isMusicMuted;
    public static boolean isSoundMuted;

    // none should not have a sound
    public enum Sounds {
        none
        , introMusic
        , warriorMusic1
        , warriorMusic2
        , warriorMusic3
        , rogueMusic1
        , rogueMusic2
        , rogueMusic3
        , clericMusic1
        , clericMusic2
        , clericMusic3
        , wizardMusic1
        , swipe
        , impact
        , die
        , collect
        , gemDrop
        , fireball
        , scorch
        , transformIntoWizard
        , warriorGemsFull
        , rogueGemsFull
        , clericGemsFull
        , thud
        , transformIntoWarrior
        , transformIntoRogue
        , transformIntoCleric
        , lightning
        , playerHit
        , playerImpact
        , playerDropGems
        , intro1
        , intro2
        , intro3
        , intro4
        , intro5
        , intro6
        , intro7
        , intro8
        , outro
        , warriorWalkout1
        , warriorWalkout2
        , warriorWalkout3
        , warriorWalkout4
        , warriorWalkout5
        , thiefWalkout1
        , thiefWalkout2
        , thiefWalkout3
        , thiefWalkout4
        , thiefWalkout5
        , clericWalkout1
        , clericWalkout2
        , clericWalkout3
        , clericWalkout4
        , clericWalkout5
        , clericWalkout6
//        ,
//        , impactLight
//        , impactWet

    }

    public enum Musics {
        none
        ,introMusic
        ,outroMusic
        ,wizardMusic1
        ,warriorMusic1
        ,warriorMusic2
        ,warriorMusic3
        ,rogueMusic1
        ,rogueMusic2
        ,rogueMusic3
        ,clericMusic1
        ,clericMusic2
        ,clericMusic3

    }

    public ObjectMap<Sounds, SoundContainer> sounds = new ObjectMap<>();
    public ObjectMap<Musics, Music> musics = new ObjectMap<>();

    public Music currentMusic;
    public Musics eCurrentMusic;
    public Music oldCurrentMusic;

    private final Assets assets;
    private final TweenManager tween;

    public AudioManager(Assets assets, TweenManager tween) {
        this.assets = assets;
        this.tween = tween;

//        putSound(Sounds.chaching, assets.chachingSound);

//        putSound(Sounds.introMusic, assets.introMusicSound);
//
//        putSound(Sounds.warriorMusic1, assets.warriorMusic1);
//        putSound(Sounds.warriorMusic2, assets.warriorMusic2);
//        putSound(Sounds.warriorMusic3, assets.warriorMusic3);
//        putSound(Sounds.rogueMusic1, assets.rogueMusic1);
//        putSound(Sounds.rogueMusic2, assets.rogueMusic2);
//        putSound(Sounds.rogueMusic3, assets.rogueMusic3);
//        putSound(Sounds.clericMusic1, assets.clericMusic1);
//        putSound(Sounds.clericMusic2, assets.clericMusic2);
//        putSound(Sounds.clericMusic3, assets.clericMusic3);
//
//        putSound(Sounds.wizardMusic1, assets.wizardMusic1);


        putSound(Sounds.swipe, assets.swipe1);
        putSound(Sounds.swipe, assets.swipe2);
        putSound(Sounds.swipe, assets.swipe3);
        putSound(Sounds.swipe, assets.swipe4);
        putSound(Sounds.swipe, assets.swipe5);

        putSound(Sounds.impact, assets.impact1);
        putSound(Sounds.impact, assets.impact2);
        putSound(Sounds.impact, assets.impact3);
        putSound(Sounds.impact, assets.impact4);
        putSound(Sounds.impact, assets.impactLight);
        putSound(Sounds.impact, assets.impactWet);


        putSound(Sounds.collect, assets.collect1);
        putSound(Sounds.collect, assets.collect2);
        putSound(Sounds.collect, assets.collect3);

        putSound(Sounds.die, assets.die1);

        putSound(Sounds.fireball, assets.fireball1);
        putSound(Sounds.fireball, assets.fireball2);
        putSound(Sounds.fireball, assets.fireball3);
        putSound(Sounds.fireball, assets.fireball4);
        putSound(Sounds.fireball, assets.fireball5);

        putSound(Sounds.scorch, assets.scorch1);
        putSound(Sounds.scorch, assets.scorch2);
        putSound(Sounds.scorch, assets.scorch3);
        putSound(Sounds.scorch, assets.scorch4);
        putSound(Sounds.lightning, assets.lightning1);
        putSound(Sounds.warriorGemsFull, assets.warriorGemsFull);
        putSound(Sounds.rogueGemsFull, assets.rogueGemsFull);
        putSound(Sounds.clericGemsFull, assets.clericGemsFull);
        putSound(Sounds.playerHit, assets.playerHit1);
        putSound(Sounds.playerHit, assets.playerHit2);
        putSound(Sounds.playerHit, assets.playerHit3);
        putSound(Sounds.playerHit, assets.playerHit4);
        putSound(Sounds.playerHit, assets.playerHit5);
        putSound(Sounds.playerHit, assets.playerHit6);
        putSound(Sounds.playerHit, assets.playerHit7);
        putSound(Sounds.playerImpact, assets.playerImpact1);
        putSound(Sounds.playerImpact, assets.playerImpact2);
        putSound(Sounds.playerImpact, assets.playerImpact3);
        putSound(Sounds.playerImpact, assets.playerImpact4);
        putSound(Sounds.playerDropGems, assets.playerDropGems1);
        putSound(Sounds.playerDropGems, assets.playerDropGems2);
        putSound(Sounds.playerDropGems, assets.playerDropGems3);
        putSound(Sounds.playerDropGems, assets.playerDropGems4);

        putSound(Sounds.intro1, assets.intro1);
        putSound(Sounds.intro2, assets.intro2);
        putSound(Sounds.intro3, assets.intro3);
        putSound(Sounds.intro4, assets.intro4);
        putSound(Sounds.intro5, assets.intro5);
        putSound(Sounds.intro6, assets.intro6);
        putSound(Sounds.intro7, assets.intro7);
        putSound(Sounds.intro8, assets.intro8);
        putSound(Sounds.outro, assets.outro);

        putSound(Sounds.warriorWalkout1, assets.warriorWalkout1);
        putSound(Sounds.warriorWalkout2, assets.warriorWalkout2);
        putSound(Sounds.warriorWalkout3, assets.warriorWalkout3);
        putSound(Sounds.warriorWalkout4, assets.warriorWalkout4);
        putSound(Sounds.warriorWalkout5, assets.warriorWalkout5);

        putSound(Sounds.thiefWalkout1, assets.thiefWalkout1);
        putSound(Sounds.thiefWalkout2, assets.thiefWalkout2);
        putSound(Sounds.thiefWalkout3, assets.thiefWalkout3);
        putSound(Sounds.thiefWalkout4, assets.thiefWalkout4);
        putSound(Sounds.thiefWalkout5, assets.thiefWalkout5);

        putSound(Sounds.clericWalkout1, assets.clericWalkout1);
        putSound(Sounds.clericWalkout2, assets.clericWalkout2);
        putSound(Sounds.clericWalkout3, assets.clericWalkout3);
        putSound(Sounds.clericWalkout4, assets.clericWalkout4);
        putSound(Sounds.clericWalkout5, assets.clericWalkout5);
        putSound(Sounds.clericWalkout6, assets.clericWalkout6);

//        putSound(Sounds.fireball, assets.fireball5);





        musics.put(Musics.introMusic, assets.introMusicMusic);
        musics.put(Musics.outroMusic, assets.outroMusic);
        musics.put(Musics.wizardMusic1, assets.wizardMusic1);

        musics.put(Musics.warriorMusic1, assets.warriorMusic1);
        musics.put(Musics.warriorMusic2, assets.warriorMusic2);
        musics.put(Musics.warriorMusic3, assets.warriorMusic3);

        musics.put(Musics.rogueMusic1, assets.rogueMusic1);
        musics.put(Musics.rogueMusic2, assets.rogueMusic2);
        musics.put(Musics.rogueMusic3, assets.rogueMusic3);

        musics.put(Musics.clericMusic1, assets.clericMusic1);
        musics.put(Musics.clericMusic2, assets.clericMusic2);
        musics.put(Musics.clericMusic3, assets.clericMusic3);


        musicVolume = new MutableFloat(0.5f);
        soundVolume = new MutableFloat(0.75f);

        isMusicMuted = false;
        isSoundMuted = false;

    }

    public void update(float dt) {
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume.floatValue());
            currentMusic.play();
        }

        if (oldCurrentMusic != null) {
            oldCurrentMusic.setVolume(musicVolume.floatValue());
        }
    }

    @Override
    public void dispose() {
        Sounds[] allSounds = Sounds.values();
        for (Sounds sound : allSounds) {
            if (sounds.get(sound) != null) {
                sounds.get(sound).dispose();
            }
        }
        Musics[] allMusics = Musics.values();
        for (Musics music : allMusics) {
            if (musics.get(music) != null) {
                musics.get(music).dispose();
            }
        }
        currentMusic = null;
    }

    public void putSound(Sounds soundType, Sound sound) {
        SoundContainer soundCont = sounds.get(soundType);
        if (soundCont == null) {
            soundCont = new SoundContainer();
        }

        soundCont.addSound(sound);
        sounds.put(soundType, soundCont);
    }

    public long playSound(Sounds soundOption) {
        if (isSoundMuted || soundOption == Sounds.none) return -1;
        return playSound(soundOption, soundVolume.floatValue());
    }

    public long playSound(Sounds soundOption, float volume) {
        volume = volume * soundVolume.floatValue();
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        return (s != null) ? s.play(volume) : 0;
    }

    public long playSound(Sounds soundOption, float volume, float pitch, float pan) {
        volume = volume * soundVolume.floatValue();
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        return (s != null) ? s.play(volume, pitch, pan) : 0;
    }

    public long playDirectionalSoundFromVector(Sounds soundOption, Vector2 vector, float viewportWidth) {
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        float midWidth = viewportWidth / 2f;
        float pan = -1 * (midWidth - vector.x) / midWidth;
//        Gdx.app.log("pan: ", String.valueOf(pan));

        return (s != null) ? s.play(soundVolume.floatValue(), 1f, pan) : 0;
    }

    public long loopSound(Sounds soundOption, float volume) {
        volume = volume * soundVolume.floatValue();
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        return (s != null) ? s.loop(volume) : 0;
    }


    public void stopSound(Sounds soundOption) {
        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont != null) {
            soundCont.stopSound();
        }
    }

    public void stopAllSounds() {
        for (SoundContainer soundCont : sounds.values()) {
            if (soundCont != null) {
                soundCont.stopSound();
            }
        }
    }

    public Music playMusic(Musics musicOptions) {
        return playMusic(musicOptions, true);
    }

    public Music playMusic(Musics musicOptions, boolean playImmediately) {
        return playMusic(musicOptions, playImmediately, true);
    }

    public Music playMusic(Musics musicOptions, boolean playImmediately, boolean looping) {
        if (playImmediately) {
            if (currentMusic != null && currentMusic.isPlaying()) {
                currentMusic.stop();
            }
            // fade in out streams
            currentMusic = startMusic(musicOptions, looping);
        } else {
            if (currentMusic == null || !currentMusic.isPlaying()) {
                currentMusic = startMusic(musicOptions, looping);
            } else {
                currentMusic.setLooping(false);
                currentMusic.setOnCompletionListener(music -> {
                    currentMusic = startMusic(musicOptions, looping);
                });
            }
        }
        return currentMusic;
    }

    private Music startMusic(Musics musicOptions, boolean looping) {
        Music music = musics.get(musicOptions);
        if (music != null) {
            music.setVolume(musicVolume.floatValue());
            music.setLooping(looping);
            music.play();
        }
        return music;
    }

    public void fadeMusic(Musics musicOption) {
        if (eCurrentMusic == musicOption) return;

    }

    public void stopMusic() {
        for (Music music : musics.values()) {
            if (music != null) music.stop();
        }
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void setMusicVolume(float level) {
        if (isMusicMuted)
            musicVolume.setValue(0f);
        else
            musicVolume.setValue(level);
    }
    public void setSoundVolume(float level) {
        if (isSoundMuted)
            soundVolume.setValue(0f);
        else
            soundVolume.setValue(level);
    }

}

class SoundContainer {
    public Array<Sound> sounds;
    public Sound currentSound;

    public SoundContainer() {
        sounds = new Array<Sound>();
    }

    public void addSound(Sound s) {
        if (!sounds.contains(s, false)) {
            sounds.add(s);
        }
    }

    public Sound getSound() {
        if (sounds.size > 0) {
            int randIndex = MathUtils.random(0, sounds.size - 1);
            Sound s = sounds.get(randIndex);
            currentSound = s;
            return s;
        } else {
            // Gdx.app.log("No sounds found!");
            return null;
        }
    }

    public void stopSound() {
        if (currentSound != null) {
            currentSound.stop();
        }
    }

    public void dispose() {
        if (currentSound != null) {
            currentSound.dispose();
        }
    }
}
