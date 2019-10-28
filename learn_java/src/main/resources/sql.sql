select
  null as id,
  dt,
  vender_id,
  robot_id,
  type,
  case
    when msg_source = 'BOT'
      then 0
    when msg_source = 'AID'
      then 1
    when msg_source = 'MAN'
      then 2
    ELSE NULL
    end as source,
  pre_num as promot_num,
  pre_n_buy_num as promot_n_buy_num,
  pre_n_ord_num promot_n_ord_num,
  case
    when pre_num != 0
      then cast(pre_n_buy_num as double) * 1.0 / cast(pre_num as double)
    else 0
    end as tranfer_ratio_pay,
  case
    when pre_num != 0
      then cast(pre_n_ord_num as double) * 1.0 / cast(pre_num as double)
    else 0
    end as tranfer_ratio_ord,
  null as sku,
  before_prefr_amount,
  after_prefr_amount,
  from_unixtime(unix_timestamp()) as create_time,
  'bdp' as crate_pin,
  1 as yn
from
  (
    select
      dt,
      vender_id,
      robot_id,
      type,
      msg_source,
      count(distinct user_pin) as pre_num,
      count(distinct
            case
              when ordin24h = 1
                and payin24h = 1
                then user_pin
              else NULL
              end) as pre_n_buy_num,
      count(distinct
            case
              when ordin24h = 1
                then user_pin
              else NULL
              end) as pre_n_ord_num,
      sum(if(payin24h = 1, before_prefr_amount, 0)) as before_prefr_amount,
      sum(if(payin24h = 1, after_prefr_amount, 0)) as after_prefr_amount
    from
      (
        select
          a.dt,
          a.vender_id,
          a.robot_id,
          a.user_pin,
          a.first_p_time,
          a.last_p_time,
          a.type,
          a.msg_source,
          ---b.*, --- put more oder relation information here like amount of pay
          b.before_prefr_amount,
          b.after_prefr_amount,
          case
            when unix_timestamp(b.pay_tm) - unix_timestamp(a.last_p_time) <= 86400
              and unix_timestamp(b.pay_tm) - unix_timestamp(a.first_p_time) > 0
              then 1
            else 0
            end as payin24h,
          case
            when unix_timestamp(b.order_time) - unix_timestamp(a.last_p_time) <= 86400
              and unix_timestamp(b.order_time) - unix_timestamp(a.first_p_time) > 0
              then 1
            else 0
            end as ordin24h
        from
          (
            select
              dt,
              vender_id,
              robot_id,
              user_pin,
              type,
              msg_source,
              min(promotion_time) as first_p_time,
              max(promotion_time) as last_p_time
            from
              app.app_jiimi3_intelligent_promotion
            where
                dt >= sysdate( - 5)
              and dt <= sysdate( - 2)
              and is_send = 1
              and type in(0, 1, 2, 3)
              and
              (
                  cast(regexp_replace(vender_id, '[a-zA-z]+', '') as bigint) is NULL --- pure brevity_code
                  or cast(regexp_replace(vender_id, '[a-zA-z]+', '') as bigint) <= 1000000000
                ) --- number less than 1000000000(10位)
            group by
              vender_id,
              robot_id,
              user_pin,
              dt,
              type,
              msg_source
          )
            a
            left join
            (
              select
                a.user_log_acct,
                a.sale_ord_valid_flag,
                a.pop_vender_id,
                a.pop_shop_id,
                a.major_supp_brevity_code,
                case
                  when a.pop_vender_id is not null
                    and length(a.pop_vender_id) > 0
                    then a.pop_vender_id
                  else a.major_supp_brevity_code
                  end as vender_id,
                a.pay_tm,
                a.sale_ord_dt,
                a.sale_ord_tm,
                a.sale_ord_id,
                a.item_sku_id,
                a.main_sku_id,
                a.user_actual_pay_amount,
                a.before_prefr_amount,
                a.after_prefr_amount,
                a.sale_qtty,
                a.ord_status_cd_1 AS state1,
                a.ord_status_cd_2 AS state2,
                a.sale_ord_tm AS order_time,
                a.ord_cancel_tm AS canceled_time,
                a.dt
              from
                gdm.gdm_m04_ord_det_sum a
              where
                  dt >= sysdate( - 5)
                and sale_ord_dt >= sysdate( - 5)
                and sale_ord_valid_flag = 1
            )
              b
            on
                  a.user_pin = b.user_log_acct
                AND a.vender_id = b.vender_id

        union

        select
          a.dt,
          a.vender_id,
          a.robot_id,
          a.user_pin,
          a.first_p_time,
          a.last_p_time,
          a.type,
          a.msg_source,
          ---b.*, --- put more oder relation information here like amount of pay
          b.before_prefr_amount,
          b.after_prefr_amount,
          case
            when unix_timestamp(b.pay_tm) - unix_timestamp(a.last_p_time) <= 86400
              and unix_timestamp(b.pay_tm) - unix_timestamp(a.first_p_time) > 0
              then 1
            else 0
            end as payin24h,
          case
            when unix_timestamp(b.order_time) - unix_timestamp(a.last_p_time) <= 86400
              and unix_timestamp(b.order_time) - unix_timestamp(a.first_p_time) > 0
              then 1
            else 0
            end as ordin24h
        from
          (
            select
              dt,
              vender_id,
              robot_id,
              user_pin,
              type,
              msg_source,
              min(promotion_time) as first_p_time,
              max(promotion_time) as last_p_time
            from
              app.app_jiimi3_intelligent_promotion
            where
                dt >= sysdate( - 5)
              and dt <= sysdate( - 2)
              and is_send = 1
              and type in(0, 1, 2, 3)
              and cast(regexp_replace(vender_id, '[a-zA-z]+', '') as bigint) > 1000000000 --- over 1000000000
            group by
              vender_id,
              robot_id,
              user_pin,
              dt,
              type,
              msg_source
          )
            a
            left join
            (
              select
                a.user_log_acct,
                a.sale_ord_valid_flag,
                a.pop_vender_id,
                a.pop_shop_id,
                a.major_supp_brevity_code,
                case
                  when a.pop_vender_id is not null
                    and length(a.pop_vender_id) > 0
                    then a.pop_vender_id
                  else a.pop_shop_id --- here using shop id as vender_id, when vender_id > 1000000000(10位)
                  end as vender_id,
                a.pay_tm,
                a.sale_ord_dt,
                a.sale_ord_tm,
                a.sale_ord_id,
                a.item_sku_id,
                a.main_sku_id,
                a.user_actual_pay_amount,
                a.before_prefr_amount,
                a.after_prefr_amount,
                a.sale_qtty,
                a.ord_status_cd_1 AS state1,
                a.ord_status_cd_2 AS state2,
                a.sale_ord_tm AS order_time,
                a.ord_cancel_tm AS canceled_time,
                a.dt
              from
                gdm.gdm_m04_ord_det_sum a
              where
                  dt >= sysdate( - 5)
                and sale_ord_dt >= sysdate( - 5)
                and sale_ord_valid_flag = 1
            )
              b
            on
                  a.user_pin = b.user_log_acct
                AND a.vender_id = b.vender_id
      )
        m
    group by
      vender_id,
      dt,
      type,
      robot_id,
      msg_source
  )
    t
