using Assets.Scripts.Commons;
using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;

namespace Assets.Scripts.IOs
{
    public static class IconBundleCache
    {
        private const int BUNDLE_FORMAT_VERSION = 1;

        private const int SAVE_BATCH_SIZE = 20;

        private static readonly object lockObj = new object();

        private static readonly Dictionary<int, sbyte[]> iconDataById = new Dictionary<int, sbyte[]>();

        private static int loadedVersion = -1;

        private static bool isLoaded;

        private static int pendingChangeCount;

        public static bool TryGetIcon(int versionImage, int iconId, out sbyte[] data)
        {
            lock (lockObj)
            {
                EnsureLoaded(versionImage);
                if (iconDataById.ContainsKey(iconId))
                {
                    data = iconDataById[iconId];
                    return true;
                }
                data = null;
                return false;
            }
        }

        public static void SaveIcon(int versionImage, int iconId, sbyte[] data)
        {
            if (data == null || data.Length == 0)
            {
                return;
            }
            lock (lockObj)
            {
                EnsureLoaded(versionImage);
                if (iconDataById.ContainsKey(iconId) && IsSameData(iconDataById[iconId], data))
                {
                    return;
                }
                iconDataById[iconId] = data;
                pendingChangeCount++;
                if (pendingChangeCount >= SAVE_BATCH_SIZE)
                {
                    FlushInternal();
                }
            }
        }

        public static void Flush()
        {
            lock (lockObj)
            {
                FlushInternal();
            }
        }

        private static void EnsureLoaded(int versionImage)
        {
            if (isLoaded && loadedVersion == versionImage)
            {
                return;
            }
            FlushInternal();
            iconDataById.Clear();
            loadedVersion = versionImage;
            isLoaded = true;
            pendingChangeCount = 0;
            LoadBundle(versionImage);
        }

        private static void FlushInternal()
        {
            if (!isLoaded || loadedVersion < 0 || pendingChangeCount <= 0)
            {
                return;
            }
            SaveBundle(loadedVersion);
            pendingChangeCount = 0;
        }

        private static bool IsSameData(sbyte[] source, sbyte[] target)
        {
            if (source == null || target == null || source.Length != target.Length)
            {
                return false;
            }
            for (int i = 0; i < source.Length; i++)
            {
                if (source[i] != target[i])
                {
                    return false;
                }
            }
            return true;
        }

        private static string GetBundlePath(int versionImage)
        {
            return Rms.GetDocumentsPath() + "/icon_bundle_" + versionImage + ".bin";
        }

        private static void LoadBundle(int versionImage)
        {
            try
            {
                string path = GetBundlePath(versionImage);
                if (!File.Exists(path))
                {
                    return;
                }
                using (FileStream fileStream = new FileStream(path, FileMode.Open, FileAccess.Read, FileShare.Read))
                using (GZipStream gzipStream = new GZipStream(fileStream, CompressionMode.Decompress))
                using (BinaryReader reader = new BinaryReader(gzipStream))
                {
                    int formatVersion = reader.ReadInt32();
                    if (formatVersion != BUNDLE_FORMAT_VERSION)
                    {
                        return;
                    }
                    int count = reader.ReadInt32();
                    for (int i = 0; i < count; i++)
                    {
                        int iconId = reader.ReadInt32();
                        int length = reader.ReadInt32();
                        if (length <= 0)
                        {
                            continue;
                        }
                        byte[] bytes = reader.ReadBytes(length);
                        if (bytes.Length != length)
                        {
                            break;
                        }
                        iconDataById[iconId] = Utils.convertToSbyteArray(bytes);
                    }
                }
            }
            catch
            {
            }
        }

        private static void SaveBundle(int versionImage)
        {
            try
            {
                Directory.CreateDirectory(Rms.GetDocumentsPath());
                string path = GetBundlePath(versionImage);
                using (FileStream fileStream = new FileStream(path, FileMode.Create, FileAccess.Write, FileShare.None))
                using (GZipStream gzipStream = new GZipStream(fileStream, CompressionMode.Compress))
                using (BinaryWriter writer = new BinaryWriter(gzipStream))
                {
                    writer.Write(BUNDLE_FORMAT_VERSION);
                    writer.Write(iconDataById.Count);
                    foreach (KeyValuePair<int, sbyte[]> entry in iconDataById)
                    {
                        writer.Write(entry.Key);
                        byte[] bytes = Utils.convertToByteArray(entry.Value);
                        writer.Write(bytes.Length);
                        writer.Write(bytes);
                    }
                }
            }
            catch
            {
            }
        }
    }
}