package com.ict.finalpj.domain.deal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealNoteVO {
  private String dealNoteIdx, dealNoteSendUserIdx, dealNoteSendUserNick, dealNoteContent, dealNoteRegdate, dealNoteReceiveUserIdx, dealNoteReceiveUserNick, dealNote01, dealNote02;
}
