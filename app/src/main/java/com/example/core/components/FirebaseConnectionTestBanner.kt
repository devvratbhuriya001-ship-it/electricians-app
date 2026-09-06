package com.example.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.FirebaseConnectionState
import com.example.data.remote.FirebaseConnectionTester
import kotlinx.coroutines.launch

@Composable
fun FirebaseConnectionTestBanner(
    modifier: Modifier = Modifier,
    autoRun: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    val state by FirebaseConnectionTester.connectionState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (autoRun && state is FirebaseConnectionState.Idle) {
            FirebaseConnectionTester.runConnectionTest()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("firebase_connection_test_banner")
            .clip(RoundedCornerShape(12.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = when (state) {
                is FirebaseConnectionState.Success -> Color(0xFFE8F5E9)
                is FirebaseConnectionState.Failed -> Color(0xFFFFEBEE)
                is FirebaseConnectionState.Testing -> Color(0xFFFFF8E1)
                is FirebaseConnectionState.Idle -> Color(0xFFF5F5F5)
            }
        ),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                when (state) {
                    is FirebaseConnectionState.Success -> Color(0xFF81C784)
                    is FirebaseConnectionState.Failed -> Color(0xFFE57373)
                    is FirebaseConnectionState.Testing -> Color(0xFFFFD54F)
                    is FirebaseConnectionState.Idle -> Color(0xFFE0E0E0)
                }
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    when (state) {
                        is FirebaseConnectionState.Testing -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFFF57F17)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Testing Firebase...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFE65100),
                                    fontSize = 13.sp
                                )
                            )
                        }
                        is FirebaseConnectionState.Success -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Firebase Connected Successfully",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20),
                                    fontSize = 13.sp
                                )
                            )
                        }
                        is FirebaseConnectionState.Failed -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Failed",
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Firebase Connection Failed",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB71C1C),
                                    fontSize = 13.sp
                                )
                            )
                        }
                        is FirebaseConnectionState.Idle -> {
                            Text(
                                text = "Firebase Status: Not Checked",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF616161),
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }

                // Action / Retry button
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            FirebaseConnectionTester.runConnectionTest()
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Re-test",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF424242)
                    )
                }
            }

            // Content section
            when (val currentState = state) {
                is FirebaseConnectionState.Success -> {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "WRITE ✓ | READ ✓ (status: ${currentState.connectionStatus})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32),
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = Color(0xFFC8E6C9),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Doc ID: ${currentState.documentId}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20),
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Collection: ${currentState.collectionName} • Package: ${currentState.packageName}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF388E3C),
                            fontSize = 10.sp
                        )
                    )
                }
                is FirebaseConnectionState.Failed -> {
                    currentState.details?.let { details ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = details,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFC62828),
                                fontSize = 11.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                FirebaseConnectionTester.runConnectionTest()
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFB71C1C)
                        ),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Retry Connection Test", fontSize = 11.sp)
                    }
                }
                is FirebaseConnectionState.Idle -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                FirebaseConnectionTester.runConnectionTest()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0288D1)
                        ),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text("Trigger Firestore Test Now", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                else -> Unit
            }
        }
    }
}
