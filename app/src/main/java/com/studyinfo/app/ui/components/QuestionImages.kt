package com.studyinfo.app.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.studyinfo.app.data.database.entity.QuestionImageEntity

/** Best-effort display source for an image row: private local copy first, cloud URL as fallback. */
private fun QuestionImageEntity.displayUri(): String? =
    localUri?.takeIf { it.isNotBlank() } ?: remoteUrl?.takeIf { it.isNotBlank() }

/* ------------------------------------------------------------------ */
/* Edit screens: attachment strip + picker                             */
/* ------------------------------------------------------------------ */

/**
 * Horizontal attachment strip for the Add/Edit question forms.
 *
 * - "+" tile launches the system photo picker (multi-select, up to [MAX_IMAGES] at a
 *   time — permission-free on every supported API level).
 * - Thumbnails carry a remove button; tapping a thumbnail opens the full-screen preview.
 * - [onPicked] hands the picked uris to the ViewModel, which imports them into private
 *   storage (see QuestionImageRepository.addFromPicker).
 */
@Composable
fun QuestionImageAttachments(
    images: List<QuestionImageEntity>,
    enabled: Boolean,
    onPicked: (List<android.net.Uri>) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGES),
    ) { uris -> if (uris.isNotEmpty()) onPicked(uris) }

    var previewUri by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Question images",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            if (images.isNotEmpty()) {
                Text(
                    text = "${images.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 6.dp),
        ) {
            items(images, key = { it.id }) { image ->
                AttachmentThumb(
                    image = image,
                    enabled = enabled,
                    onRemove = { onRemove(image.id) },
                    onOpen = { image.displayUri()?.let { previewUri = it } },
                )
            }
            if (enabled) {
                item(key = "add-image") {
                    AddImageTile(
                        onClick = {
                            picker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        },
                    )
                }
            }
        }
    }

    ImagePreviewDialog(uri = previewUri, onDismiss = { previewUri = null })
}

@Composable
private fun AddImageTile(onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .size(76.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Filled.AddPhotoAlternate,
                contentDescription = "Add image",
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Add",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AttachmentThumb(
    image: QuestionImageEntity,
    enabled: Boolean,
    onRemove: () -> Unit,
    onOpen: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(76.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onOpen),
    ) {
        AsyncImage(
            model = image.displayUri(),
            contentDescription = "Question image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        if (enabled) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier
                    .padding(4.dp)
                    .size(20.dp)
                    .align(Alignment.TopEnd)
                    .clickable(onClick = onRemove),
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Remove image",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(3.dp),
                )
            }
        }
    }
}

/* ------------------------------------------------------------------ */
/* Detail screens: gallery                                             */
/* ------------------------------------------------------------------ */

/** Full-width, tappable gallery of a question's attached images (detail screens). */
@Composable
fun QuestionImageGallery(
    images: List<QuestionImageEntity>,
    modifier: Modifier = Modifier,
) {
    if (images.isEmpty()) return
    var previewUri by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        images.forEach { image ->
            AsyncImage(
                model = image.displayUri(),
                contentDescription = "Question image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { image.displayUri()?.let { previewUri = it } },
            )
        }
    }

    ImagePreviewDialog(uri = previewUri, onDismiss = { previewUri = null })
}

/* ------------------------------------------------------------------ */
/* Full-screen preview                                                 */
/* ------------------------------------------------------------------ */

@Composable
private fun ImagePreviewDialog(uri: String?, onDismiss: () -> Unit) {
    if (uri == null) return
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = uri,
                contentDescription = "Question image (full screen)",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
            Text(
                text = "Tap anywhere to close",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
            )
        }
    }
}

private const val MAX_IMAGES = 4
